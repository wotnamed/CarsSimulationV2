import java.awt.*;

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
    // variables
    protected double fuel;

    public Tire getTire() {
        return tire;
    }

    public double getVehicleDrag() {
        return vehicleDrag;
    }

    public double getVehicleTraction() {
        return vehicleTraction;
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
    }

    public void updateGroundParameters(Color groundColour, Map map){
        int index = -1; // index in list or array cannot be negative
        for (int i = 0; i < map.getGroundColourMap().length; i++){
            if (map.getGroundColourMap()[i].equals(groundColour)){
                index = i;
            }
        }
        if (index != -1){
            this.groundDrag = map.getGroundDragMap()[index];
            this.groundTraction = map.getGroundTractionMap()[index];
        } else {
            System.out.println("ground not found!");
        }
    }
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
        System.out.println(this.mass);
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
}
