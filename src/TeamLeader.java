public class TeamLeader implements RaceObserver {

    private Racecar racecar;
    private Checkpoint checkpoint;
    private Tanker tanker;
    private TireChanger tireChanger;
    private Tire newTire;
    private Pitstop pitstop;

    public TeamLeader(Racecar racecar, Checkpoint checkpoint, Tanker tanker, TireChanger tireChanger, Pitstop pitstop) {
        this.racecar = racecar;
        this.checkpoint = checkpoint;
        this.tanker = tanker;
        this.tireChanger = tireChanger;
        this.pitstop = pitstop;
    }

    // Inside TeamLeader.java
    @Override
    public void onRaceEvent(Racecar racecar, RaceEvent event) {
        if (event == RaceEvent.POSITION_UPDATED) {
            if (racecar.getFuel() < 0.6 * racecar.getMaxFuel() || racecar.getTire().getDurability() < 0.6) {
                racecar.setWantsToPit(true);
            }

            double[] checkpointCoordinates = checkpoint.getCoordinates();
            double[] vehicleCoordinates = racecar.getCurrentCoordinates();
            double distance = Math.sqrt(Math.pow(checkpointCoordinates[0] - vehicleCoordinates[0], 2) + Math.pow(checkpointCoordinates[1] - vehicleCoordinates[1], 2));

            if (distance < 10) {
                this.newTire = new Tire(0.8, "Sigma-New", 9000);
                if (!racecar.isPitStopping && !racecar.justFinishedPitStop){
                    pitstop.startPitStop(tireChanger, tanker, newTire, racecar);
                }
            }
        }
    }
}