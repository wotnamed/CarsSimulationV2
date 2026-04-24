public class Pitstop {

    public void pitstop(Racecar racecar, double dt, Tanker tanker, TireChanger tireChanger, Tire tire){
        //delay based on tire change speed and fuel change speed/fuel level
        tireChanger.changeTire(racecar, tire);
        tanker.refuel(racecar, dt);
    }
}
