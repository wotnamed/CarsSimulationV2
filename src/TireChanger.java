import java.awt.*;

public class TireChanger extends Vehicle {
    private double skillLevel;

    public TireChanger(double[] coordinates, int[] primaryColor, int[] secondaryColor, double skillLevel) {
        this.currentCoordinates = coordinates;
        this.primaryColour = new Color(primaryColor[0], primaryColor[1], primaryColor[2]);
        this.secondaryColour = new Color(secondaryColor[0], secondaryColor[1], secondaryColor[2]);
        this.dimensions = new int[]{20, 20};
        this.facingAngleRad = 0;

        this.skillLevel = skillLevel;
    }

    public void changeTire(Racecar racecar, Tire newTire) {
        racecar.setTire(newTire);
        racecar.nextTire = null;
    }

    // Calculate stop time
    public double calculateTireChangeTime() {
        double baseTimeSeconds = 5.0;
        return baseTimeSeconds / skillLevel;
    }
}