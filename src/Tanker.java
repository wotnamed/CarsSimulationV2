import javax.swing.*;
import java.awt.*;

public class Tanker extends Vehicle{
    public Tanker(double[] coordinates, int[] primaryColor, int[] secondaryColor){
        this.currentCoordinates = coordinates;
        this.primaryColour = new Color(primaryColor[0], primaryColor[1], primaryColor[2]);
        this.secondaryColour = new Color(secondaryColor[0], secondaryColor[1], secondaryColor[2]);
        this.dimensions = new int[]{20,20};
        this.facingAngleRad = 0;

    }
    public void refuel(Racecar racecar, double dt){
                racecar.setFuel(1);
    }
}