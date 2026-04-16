import java.awt.*;

public abstract class Vehicle {
    protected double[] currentCoordinates;
    protected Color primaryColour;
    protected Color secondaryColour;
    protected double facingAngleRad;
    protected int[] dimensions;

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
