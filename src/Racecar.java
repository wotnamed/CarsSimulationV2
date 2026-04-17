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

    public Racecar(int[] primaryColor, int[] secondaryColor, double facingAngleRad, double[] currentCoordinates, int[] dimensions){
        this.primaryColour = new Color(primaryColor[0], primaryColor[1], primaryColor[2]);
        this.secondaryColour = new Color(secondaryColor[0], secondaryColor[1], secondaryColor[2]);
        this.facingAngleRad = facingAngleRad;
        this.currentCoordinates = currentCoordinates;
        this.dimensions = dimensions;

        this.velocity = 0;
        this.vehicleTraction = 0.1;
        this.groundTraction = 1;
        this.mass = 1.5;
        this.collisionVelocityVector = new double[]{0,0};
        this.vehicleDrag = 0.005;
        this.groundDrag = 0;
        this.enginePower = 10;
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
