import java.awt.*;
import java.util.HashMap;

public abstract class PhysicsBasedVehicle extends Vehicle{
    protected double mass;
    protected double enginePower;
    protected double vehicleTraction;
    protected double groundTraction;
    protected double vehicleDrag;
    protected double groundDrag;
    protected double velocity;
    protected double[] collisionVelocityVector;
    protected Tire tire;
    protected HashMap<Color, double[]> groundCache;


    public void setTire(Tire tire) {
        this.tire = tire;
    }
    // LEGACY CODE?
    public void setGroundDrag(Color groundColor) {
        if (groundColor.equals(new Color(30, 120, 30))) {
            this.groundDrag = 0.05; }// offroad
        else {
            this.groundDrag = 0.0; }// track
    }
    public void setMass(double mass) {
        this.mass = mass;
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
    public double getVehicleDrag() {
        return vehicleDrag;
    }
    public double getVehicleTraction() {
        return vehicleTraction;
    }
    public Tire getTire() {
        return tire;
    }
    public double getDrag() {
        return vehicleDrag+groundDrag;
    }
    public double getTraction() {
        return vehicleTraction*groundTraction;
    }
    public double getEnginePower() {
        return enginePower;
    }
    public double getMass() {
        return mass;
    }
    public double getCurrentVelocity() {
        return velocity;
    }

}
