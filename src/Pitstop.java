public class Pitstop {

    public void startPitStop(TireChanger tireChanger, Tanker tanker, Tire newTire, Racecar racecar) {
        if (racecar.isPitStopping) return;
        racecar.isPitStopping = true;
        racecar.wantsToPit = false;
        racecar.nextTire = newTire;
        racecar.velocity = 0;

        double tireTime = tireChanger.calculateTireChangeTime();
        double refuelTime = tanker.calculateRefuelTime(racecar.fuel, racecar.maxFuel);
        racecar.pitStopTimeRemaining = Math.max(tireTime, refuelTime);
    }

    public void PitStop(double dt, Racecar racecar, TireChanger tireChanger, Tanker tanker) {
        if (racecar.isPitStopping) {
            racecar.pitStopTimeRemaining -= dt;
            racecar.velocity = 0;

            if (racecar.pitStopTimeRemaining <= 0) {
                tireChanger.changeTire(racecar, racecar.nextTire);
                tanker.refuel(racecar);

                racecar.isPitStopping = false;
                racecar.justFinishedPitStop = true;
                racecar.wantsToPit = false;

                racecar.notifyObservers(RaceEvent.PIT_STOP_COMPLETED);
            }
        }
    }
}