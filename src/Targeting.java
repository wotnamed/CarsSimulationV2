public class Targeting {
    static void updateTargetCheckpoint(Racecar racecar, Checkpoint[] checkpointMap, double threshold){
        // takes vehicle and checkpoint map
        double[] currentCheckpointCoordinates = checkpointMap[racecar.getCheckpointIndex()].getCoordinates();
        double[] currentVehicleCoordinates = racecar.getCurrentCoordinates();
        double ds = Math.sqrt(Math.pow(currentCheckpointCoordinates[0]-currentVehicleCoordinates[0], 2) + Math.pow(currentCheckpointCoordinates[1]-currentVehicleCoordinates[1], 2));
        if (ds<threshold){
            if (racecar.getCheckpointIndex() >= checkpointMap.length-1){
                racecar.setCheckpointIndex(0);
            } else {
                racecar.setCheckpointIndex(racecar.getCheckpointIndex()+1);
            }
        }
    }
}
