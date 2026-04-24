import java.awt.*;

public class Map {
    protected String mapTitle;
    // Variables to map ground Colour to friction
    protected Color[] groundColourMap;

    public double[] getGroundDragMap() {
        return groundDragMap;
    }

    public String getMapTitle() {
        return mapTitle;
    }

    public Color[] getGroundColourMap() {
        return groundColourMap;
    }

    public double[] getGroundTractionMap() {
        return groundTractionMap;
    }

    public Checkpoint[] getCheckpointMap() {
        return checkpointMap;
    }

    public int[] getMainOval() {
        return mainOval;
    }

    public int[] getPitArea() {
        return pitArea;
    }

    protected double[] groundDragMap;
    protected double[] groundTractionMap;
    protected Checkpoint[] checkpointMap;
    protected int[] mainOval; // x, y, width, height, colourIndex, thickness,
    protected int[] pitArea; // x, y, width, height, colourIndex

    public Map(Color[] groundColourMap, double[] groundDragMap, double[] groundTractionMap, Checkpoint[] checkpointMap, int[] mainOval, int[] pitArea){
        this.groundColourMap = groundColourMap;
        this.groundDragMap = groundDragMap;
        this.groundTractionMap = groundTractionMap;
        this.checkpointMap = checkpointMap;
        this.mainOval = mainOval;
        this.pitArea = pitArea;
    }


}
