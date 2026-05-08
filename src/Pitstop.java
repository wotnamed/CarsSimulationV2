public class Pitstop {

    public void startPitStop(TireChanger tireChanger, Tanker tanker, Tire newTire, Racecar racecar) {
        racecar.isPitStopping = true;
        racecar.nextTire = newTire;

        racecar.velocity = 0;
        racecar.enginePower = 0;

        double tireTime = tireChanger.calculateTireChangeTime();
        double refuelTime = tanker.calculateRefuelTime(racecar.fuel, racecar.maxFuel);
        racecar.pitStopTimeRemaining = Math.max(tireTime, refuelTime);
    }

    public void PitStop(double dt, Racecar racecar, TireChanger tireChanger, Tanker tanker) {
        while (racecar.isPitStopping) {
            racecar.pitStopTimeRemaining -= dt;

            if (racecar.pitStopTimeRemaining <= 0) {
                tireChanger.changeTire(racecar, racecar.nextTire);
                tanker.refuel(racecar);
                racecar.isPitStopping = false;
            }
        }
    }
}