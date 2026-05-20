import java.awt.*;
import java.util.HashMap;

public class Racecar extends PhysicsBasedVehicle{
    // variables
    protected boolean isPitStopping = false;
    protected double pitStopTimeRemaining = 0.0;
    protected Tire nextTire = null;
    protected double maxFuel = 1.0;
    protected double fuel;
    // target checkpoint index in map
    protected int checkpointIndex;
    protected int lapCount;
    protected int teamIdentifier;
    // getters
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
    }
    public void setLapCount(int lapCount) {
        this.lapCount = lapCount;
    }
    // init
    public Racecar(Color primaryColor, Color secondaryColor, double facingAngleRad, double[] currentCoordinates, int[] dimensions, Tire tire, double vehicleDrag, double vehicleTraction, double enginePower, double mass, int teamIdentifier, double maxFuel){
        this.primaryColour = primaryColor;
        this.secondaryColour = secondaryColor;
        this.facingAngleRad = facingAngleRad;
        this.currentCoordinates = currentCoordinates;
        this.dimensions = dimensions;

        this.maxFuel = maxFuel;
        this.fuel = 1;
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
        this.enginePower = enginePower; // redundant assignment but this is where we would update enginePower if needed.
        double targetAngle = physics.calculateTargetAngle(currentCoordinates, targetPosition);
        this.groundTraction = groundTraction; // update groundTraction here
        this.groundDrag = groundDrag; // update groundDrag here
        this.vehicleTraction = this.tire.getTractionParameter(); // update vehicleTraction here
        double traction = this.groundTraction*this.vehicleTraction;
        double drag = this.vehicleDrag+this.groundDrag;
        double[] engineForceVector = physics.calculateEngineForceVector(traction, enginePower, targetAngle);
        double[] accelerationVector = physics.calculateAcceleration(engineForceVector, mass);
        double[] velocityVector = physics.calculateVelocityFromAcceleration(accelerationVector, dt);
        double[] summedVelocityVector = physics.sumVectors(previousVelocityVector, velocityVector);
        double[] summedVelocityVector2 = physics.sumVectors(summedVelocityVector, collisionVelocityVector);
        double absoluteVelocity = physics.calculateHypotenuse(summedVelocityVector2);
        double velocityAngle = physics.calculateAngleOfVector(summedVelocityVector2);
        double[] dragVector = physics.calculateDragVector(absoluteVelocity, drag, velocityAngle);
        double[] summedVelocityVector3 = physics.sumVectors(summedVelocityVector2, dragVector);
        double absoluteVelocity2 = physics.calculateHypotenuse(summedVelocityVector3);
        this.velocity = absoluteVelocity2;
        this.facingAngleRad = velocityAngle;
        this.currentCoordinates = physics.calculateCoordinates(currentCoordinates, summedVelocityVector3, dt);
        this.tire.update(absoluteVelocity2*dt);
        this.consumeFuel(dt);
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
            }
        }
    }

    public void consumeFuel(double dt){
        double fuelStart = this.fuel;
        this.fuel = fuel - 0.00001*enginePower*velocity*dt;
        if (this.fuel < 0) this.enginePower = 0;
        setMass(fuelStart);
    }
}
