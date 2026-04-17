import java.awt.*;

public class TireChanger extends Vehicle{
    public TireChanger(double[] coordinates, int[] primaryColor, int[] secondaryColor){
        this.currentCoordinates = coordinates;
        this.primaryColour = new Color(primaryColor[0], primaryColor[1], primaryColor[2]);
        this.secondaryColour = new Color(secondaryColor[0], secondaryColor[1], secondaryColor[2]);
        this.dimensions = new int[]{20,20};
        this.facingAngleRad = 0;
    }
    public Racecar changeTire(Racecar racecar, Tire tire){
        return new Racecar(racecar.getPrimaryColour(), racecar.getSecondaryColour(), racecar.getFacingAngleRad(), racecar.getCurrentCoordinates(), racecar.getDimensions(), tire, racecar.getVehicleDrag(), racecar.getVehicleTraction(), racecar.getEnginePower(), racecar.getMass());
    }
}
