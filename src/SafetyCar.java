import java.awt.Color;

public class SafetyCar extends Vehicle {
    private boolean isDeployed;
    private double enforcedSpeedLimit;

    public SafetyCar(double[] coordinates, Color primaryColor, Color secondaryColor, double enforcedSpeedLimit) {
        this.currentCoordinates = coordinates;
        this.primaryColour = primaryColor;
        this.secondaryColour = secondaryColor;
        this.dimensions = new int[]{50, 20};
        this.facingAngleRad = 0;
        this.enforcedSpeedLimit = enforcedSpeedLimit;
        this.isDeployed = false;
    }

    public void enforceSpeedLimit(Racecar car, double[] oldCoords, double dt) {
        if (!this.isDeployed) {
            return;
        }

        double[] newCoords = car.getCurrentCoordinates();
        double dx = newCoords[0] - oldCoords[0];
        double dy = newCoords[1] - oldCoords[1];
        double distanceMoved = Math.sqrt(dx * dx + dy * dy);

        double maxAllowedDistance = this.enforcedSpeedLimit * dt;

        if (distanceMoved > maxAllowedDistance && distanceMoved > 0) {
            double scaleFactor = maxAllowedDistance / distanceMoved;
            newCoords[0] = oldCoords[0] + (dx * scaleFactor);
            newCoords[1] = oldCoords[1] + (dy * scaleFactor);
            car.velocity = this.enforcedSpeedLimit;
        }
    }

    public void deploy() {
        this.isDeployed = true;
        System.out.println("YELLOW FLAG");
    }

    public void recall() {
        this.isDeployed = false;
        System.out.println("GREEN FLAG");
    }

    public boolean isDeployed() {
        return isDeployed;
    }

    public double getEnforcedSpeedLimit() {
        return enforcedSpeedLimit;
    }

    public void setEnforcedSpeedLimit(double enforcedSpeedLimit) {
        this.enforcedSpeedLimit = enforcedSpeedLimit;
    }
}