import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Racecar extends PhysicsBasedVehicle{
    //observer
    private final List<RaceObserver> observers = new ArrayList<>();
    // variables
    protected boolean isPitStopping = false;
    protected double pitStopTimeRemaining = 0.0;
    protected boolean justFinishedPitStop = false;
    public boolean wantsToPit = false;
    protected Tire nextTire = null;
    protected double maxFuel = 1.0;
    protected double fuel;
    // target checkpoint index in map
    protected int checkpointIndex;
    protected int lapCount;
    protected int teamIdentifier;
    // getters
    public boolean getWantsToPit() {
        return this.wantsToPit;
    }
    public int getLapCount() {
        return lapCount;
    }
    public int getCheckpointIndex() {
        return checkpointIndex;
    }
    public int getTeamIdentifier() {
        return teamIdentifier;
    }
    public double getFuel() {
        return this.fuel;
    }
    public double getMaxFuel() {
        return maxFuel;
    }

    public void addObserver(RaceObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(RaceObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(RaceEvent event) {
        for (RaceObserver observer : observers) {
            observer.onRaceEvent(this, event);
        }
    }

    // setters
    @Override
    public void setMass(double fuel) {
        this.mass = mass + this.fuel - fuel;
    }
    public void setFuel(double fuel) {
        this.fuel = fuel;
    }
    public void setTeamIdentifier(int teamIdentifier) {
        this.teamIdentifier = teamIdentifier;
    }
    public void setCheckpointIndex(int checkpointIndex) {
        this.checkpointIndex = checkpointIndex;
        notifyObservers(RaceEvent.CHECKPOINT_PASSED);
    }
    public void setLapCount(int lapCount) {
        this.lapCount = lapCount;
        notifyObservers(RaceEvent.LAP_COMPLETED);
    }
    public void setWantsToPit(boolean wantsToPit) {
        this.wantsToPit = wantsToPit;
    }

    // init
    public Racecar(Color primaryColor, Color secondaryColor, double facingAngleRad, double[] currentCoordinates, int[] dimensions, Tire tire, double vehicleDrag, double vehicleTraction, double enginePower, double mass, int teamIdentifier, double maxFuel){
        this.primaryColour = primaryColor;
        this.secondaryColour = secondaryColor;
        this.facingAngleRad = facingAngleRad;
        this.currentCoordinates = currentCoordinates;
        this.dimensions = dimensions;

        this.maxFuel = maxFuel;
        this.fuel = maxFuel;
        this.velocity = 0;
        this.groundTraction = 1;
        this.collisionVelocityVector = new double[]{0,0};
        this.groundDrag = 0;
        this.mass = mass;
        this.vehicleDrag = vehicleDrag;
        this.enginePower = enginePower;
        this.tire = tire;
        this.vehicleTraction = tire.getTractionParameter();

        this.checkpointIndex = 0;
        this.lapCount = 0;
        this.teamIdentifier = teamIdentifier;

    }
    // logic
    public void updatePosition(Physics physics, double dt, double[] targetPosition){
        double[] previousVelocityVector = physics.convertVelocityToVector(velocity, facingAngleRad);
        double engineOutput = this.enginePower;
        if (isPitStopping){
            engineOutput = 0;
        }
        double targetAngle = physics.calculateTargetAngle(currentCoordinates, targetPosition);
        this.groundTraction = groundTraction; // update groundTraction here
        this.groundDrag = groundDrag; // update groundDrag here
        this.vehicleTraction = this.tire.getTractionParameter(); // update vehicleTraction here
        double traction = this.groundTraction*this.vehicleTraction;
        double drag = this.vehicleDrag+this.groundDrag;
        double[] engineForceVector = physics.calculateEngineForceVector(traction, engineOutput, targetAngle);
        double[] accelerationVector = physics.calculateAcceleration(engineForceVector, mass);
        double[] velocityVector = physics.calculateVelocityFromAcceleration(accelerationVector, dt);
        double[] summedVelocityVector = physics.sumVectors(previousVelocityVector, velocityVector);
        double[] summedVelocityVector2 = physics.sumVectors(summedVelocityVector, collisionVelocityVector);
        double absoluteVelocity = physics.calculateHypotenuse(summedVelocityVector2);
        double velocityAngle = physics.calculateAngleOfVector(summedVelocityVector2);
        double[] dragVector = physics.calculateDragVector(absoluteVelocity, drag, velocityAngle);
        // fix edge case where drag vector is larger than engine vector
        double[] summedVelocityVector3;
        if (physics.calculateHypotenuse(summedVelocityVector2) >= physics.calculateHypotenuse(dragVector)){ // normal conditions
            summedVelocityVector3 = physics.sumVectors(summedVelocityVector2, dragVector);
        } else { // when pitting at high speeds
            summedVelocityVector3 = physics.sumVectors(summedVelocityVector2, dragVector);
            summedVelocityVector3 = new double[]{-0.01*summedVelocityVector3[0], -0.01*summedVelocityVector3[1]}; // This is done to resolve a bug where the Racecar "bounces" in and out of the pit at the pit entrance.
        }
        // update velocityAngle again to visualize actual direction (could be removed)
        velocityAngle = physics.calculateAngleOfVector(summedVelocityVector3);
        double absoluteVelocity2 = physics.calculateHypotenuse(summedVelocityVector3);
        this.velocity = absoluteVelocity2;
        this.facingAngleRad = velocityAngle;
        this.currentCoordinates = physics.calculateCoordinates(currentCoordinates, summedVelocityVector3, dt);
        this.tire.update(absoluteVelocity2*dt);
        this.consumeFuel(dt);
        if (this.fuel>0.1) {this.justFinishedPitStop = false;}
        //System.out.println(this.tire.getDurability());
    }

    public void updatePitStop(double dt) {
        if (isPitStopping) {
            pitStopTimeRemaining -= dt;
            //System.out.println("Pit stop time remaining: " + pitStopTimeRemaining);

            if (pitStopTimeRemaining <= 0) {
                this.tire = nextTire;
                this.nextTire = null;

                double previousFuel = this.fuel;
                this.fuel = this.maxFuel;
                this.setMass(previousFuel);

                this.isPitStopping = false;
                notifyObservers(RaceEvent.PIT_STOP_COMPLETED);
            }
        }
    }

    public void consumeFuel(double dt){
        double fuelStart = this.fuel;
        this.fuel = fuel - 0.00001*velocity*dt;
        if (this.fuel < 0) this.enginePower = 0;
        setMass(fuelStart);

        if (fuelStart >= 0.25 * maxFuel && this.fuel < 0.25 * maxFuel) {
            notifyObservers(RaceEvent.LOW_FUEL);
        }
    }
}
