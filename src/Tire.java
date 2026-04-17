public class Tire {
    protected double stockTractionParameter;
    protected double tractionParameter;
    protected String tireType;
    protected double distanceTraveled;
    protected double maximumDistance;
    protected double durability;

    public Tire(double tractionParameter, String tireType, double maximumDistance){
        this.stockTractionParameter = tractionParameter;
        this.tractionParameter = stockTractionParameter;
        this.tireType = tireType;
        this.maximumDistance = maximumDistance;
        this.distanceTraveled = 0;
        this.durability = 1;
    }

    public void update(double addedDistance){
        this.distanceTraveled = this.distanceTraveled + addedDistance;
        this.durability = 1-1/(maximumDistance/distanceTraveled);
        this.tractionParameter = stockTractionParameter*(0.664385*Math.log10(durability+1)+0.8);
    }

    public String getTireType(){return tireType;}
    public double getTractionParameter(){return tractionParameter;}
}
