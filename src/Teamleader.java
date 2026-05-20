public class Teamleader {

    private Racecar racecar;
    private Checkpoint checkpoint;
    private Tanker tanker;
    private TireChanger tireChanger;
    private Tire newTire;
    private Pitstop pitstop;

    public Teamleader(Racecar racecar, Checkpoint checkpoint, Tanker tanker, TireChanger tireChanger, Pitstop pitstop) {
        this.racecar = racecar;
        this.checkpoint = checkpoint;
        this.tanker = tanker;
        this.tireChanger = tireChanger;
        this.pitstop = pitstop;
    }

    public void statusCheck() {
        double[] checkpointCoordinates = checkpoint.coordinates;
        double[] vehicleCoordinates = racecar.getCurrentCoordinates();
        double distance = Math.sqrt(Math.pow(checkpointCoordinates[0] - vehicleCoordinates[0], 2) + Math.pow(checkpointCoordinates[1] - vehicleCoordinates[1], 2));
        if (distance < 25) {
            this.newTire = new Tire(0.8, "Sigma-New", 9000);
            pitstop.startPitStop(tireChanger, tanker, newTire, racecar);
        }
    }
}