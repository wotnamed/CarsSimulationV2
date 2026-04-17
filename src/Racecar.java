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

        this.velocity = 0;
        this.groundTraction = 1;
        this.collisionVelocityVector = new double[]{0,0};
        this.groundDrag = 0;
        this.mass = mass;
        this.vehicleDrag = vehicleDrag;
        this.enginePower = enginePower;
        this.tire = tire;
        this.vehicleTraction = vehicleTraction;

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
        this.vehicleTraction = vehicleTraction; // update vehicleTraction here
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
    }

    @Override
    public double getMass() {
        return mass;
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
}
