import java.awt.Color;

public class Tanker extends Vehicle {
    private double fuelPerSecond;

    public Tanker(double[] coordinates, int[] primaryColor, int[] secondaryColor, double fuelPerSecond) {
        this.currentCoordinates = coordinates;
        this.primaryColour = new Color(primaryColor[0], primaryColor[1], primaryColor[2]);
        this.secondaryColour = new Color(secondaryColor[0], secondaryColor[1], secondaryColor[2]);
        this.dimensions = new int[]{20, 20};
        this.facingAngleRad = (double)0.0F;
        this.fuelPerSecond = fuelPerSecond;
    }

    public double calculateRefuelTime(double currentFuel, double maxFuel) {
        double fuelNeeded = maxFuel - currentFuel;
        return fuelNeeded <= (double)0.0F ? (double)0.0F : fuelNeeded / this.fuelPerSecond;
    }

    public void refuel(Racecar racecar) {
        double oldFuel = racecar.fuel;
        racecar.setFuel(racecar.getMaxFuel());
        racecar.setMass(oldFuel);
    }

    public double getFuelPerSecond() {
        return this.fuelPerSecond;
    }
}
