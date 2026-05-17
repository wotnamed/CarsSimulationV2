import java.awt.*;

public abstract class Vehicle {
    // variables needed to display any "Vehicle"
    protected double[] currentCoordinates;
    protected Color primaryColour;
    protected Color secondaryColour;
    protected double facingAngleRad;
    protected int[] dimensions;
    // getters
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

}
