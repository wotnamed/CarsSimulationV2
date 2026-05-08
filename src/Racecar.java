import java.awt.*;
import java.util.HashMap;

public class Racecar extends Vehicle implements PhysicsBasedVehicle{
    protected double[] currentCoordinates;
    protected Color primaryColour;
    protected Color secondaryColour;
    protected double facingAngleRad;
    protected int[] dimensions;
    // physics
    protected double mass;
    protected double enginePower;
    protected double vehicleTraction;
    protected double groundTraction;
    protected double vehicleDrag;
    protected double groundDrag;
    protected double velocity;
    protected double[] collisionVelocityVector;
    protected Tire tire;
    protected boolean isPitStopping = false;
    protected double pitStopTimeRemaining = 0.0;
    protected Tire nextTire = null;
    protected double maxFuel = 1.0;
    // variables
    protected double fuel;
    // target checkpoint index in map
    protected int checkpointIndex;

    private HashMap<Color, double[]> groundCache;

    public Tire getTire() {
        return tire;
    }

    public double getVehicleDrag() {
        return vehicleDrag;
    }

    public double getVehicleTraction() {
        return vehicleTraction;
    }

    public int getCheckpointIndex() {
        return checkpointIndex;
    }

    public void setCheckpointIndex(int checkpointIndex) {
        this.checkpointIndex = checkpointIndex;
    }

    public Racecar(Color primaryColor, Color secondaryColor, double facingAngleRad, double[] currentCoordinates, int[] dimensions, Tire tire, double vehicleDrag, double vehicleTraction, double enginePower, double mass){
        this.primaryColour = primaryColor;
        this.secondaryColour = secondaryColor;
        this.facingAngleRad = facingAngleRad;
        this.currentCoordinates = currentCoordinates;
        this.dimensions = dimensions;

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

    }
    public Color getPrimaryColour() {
        return primaryColour;
    }

    public double[] getCurrentCoordinates() {
        return currentCoordinates;
    }

    public Color getSecondaryColour() {
        return secondaryColour;
    }

    public double getFacingAngleRad() {
        return facingAngleRad;
    }

    public int[] getDimensions() {
        return dimensions;
    }

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

    public void updateGroundParameters(Color groundColour, Map map) {
        //Lazy Initialization
        if (groundCache == null) {
            groundCache = new HashMap<>();
            Color[] colors = map.getGroundColourMap();
            double[] drags = map.getGroundDragMap();
            double[] tractions = map.getGroundTractionMap();

            // Populate the cache
            for (int i = 0; i < colors.length; i++) {
                groundCache.put(colors[i], new double[]{drags[i], tractions[i]});
            }
        }

        // Get color from map and update ground parameters
        double[] groundData = groundCache.get(groundColour);

        if (groundData != null) {
            this.groundDrag = groundData[0];
            this.groundTraction = groundData[1];
        } else {
            System.out.println("ground not found!"); // Happens when switching sometimes
        }
    }
    // LEGACY CODE
    public void setGroundDrag(Color groundColor) {
        if (groundColor.equals(new Color(30, 120, 30))) {
            this.groundDrag = 0.05; }// offroad
        else {
            this.groundDrag = 0.0; }// track
    }

    public void consumeFuel(double dt){
        double fuelStart = this.fuel;
        this.fuel = fuel - 0.00001*enginePower*velocity*dt;
        if (this.fuel < 0) this.enginePower = 0;
        setMass(fuelStart);
    }

    @Override
    public double getMass() {
        return mass;
    }

    public void setMass(double fuel) {
        this.mass = mass + this.fuel - fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    @Override
    public double getEnginePower() {
        return enginePower;
    }

    @Override
    public double getTraction() {
        return vehicleTraction*groundTraction;
    }

    @Override
    public double getDrag() {
        return vehicleDrag+groundDrag;
    }

    @Override
    public double getCurrentVelocity() {
        return velocity;
    }

    public double getFuel() {
        return this.fuel;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
    }
}
