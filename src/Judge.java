import java.awt.*;

// keep track of laps of racecars, call start of race and end of race, feed race results into team statistics (external system)
public class Judge extends Vehicle implements RaceObserver {
    public Team[] getTeamList() {
        return teamList;
    }

    public int getLapCount() {
        return lapCount;
    }

    public void setLapCount(int lapCount) {
        this.lapCount = lapCount;
    }

    public void setTeamList(Team[] teamList) {
        this.teamList = teamList;
    }
    private static Judge INSTANCE;
    protected Team[] teamList;
    protected int lapCount; // threshold for win condition
    protected boolean raceRunning;

    private Judge(){
        this.primaryColour = new Color(15,15,15);
        this.secondaryColour = new Color(200,200,200);
        this.currentCoordinates = new double[]{100,100};
        this.facingAngleRad = 1;
        this.dimensions = new int[]{40,40};
        this.teamList = new Team[]{};
        this.raceRunning = true;
    }
    public static Judge getJudge(){
        if (INSTANCE == null){
            INSTANCE = new Judge();
        } return  INSTANCE;
    }
    @Override
    public void onRaceEvent(Racecar racecar, RaceEvent event) {
        // We only care if a lap was completed and the race is still running
        if (event == RaceEvent.LAP_COMPLETED && raceRunning) {

            // Check if THIS specific car has crossed the win threshold
            if (racecar.getLapCount() >= this.lapCount) {
                this.raceRunning = false; // Stop the race updates
                int identifier = racecar.getTeamIdentifier();

                System.out.println("Judge Alert: Race finished! Winner is Team " + identifier);
                updateRaceStatistics(identifier);
                updateWinRates();
            }
        }
    }

    public void updateRaceStatistics(int winningTeamIdentifier){
        for (int e = 0; e < teamList.length; e++){
            if (winningTeamIdentifier == teamList[e].getIdentifier()){
                teamList[e].setRaceStatistics(new int[]{teamList[e].getRaceStatistics()[0]+1, teamList[e].getRaceStatistics()[1]});
            } else {
                teamList[e].setRaceStatistics(new int[]{teamList[e].getRaceStatistics()[0], teamList[e].getRaceStatistics()[1]+1});
            }
        }
    }

    public void updateWinRates(){
        for (int i = 0; i < teamList.length; i++){
            if (teamList[i].getRaceStatistics()[1] != 0){
                teamList[i].setRating(teamList[i].getRaceStatistics()[0]/teamList[i].getRaceStatistics()[1]);
            } else {
                teamList[i].setRating(0);
            }
        }
    }
}
