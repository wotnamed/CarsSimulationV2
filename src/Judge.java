import java.awt.*;

// keep track of laps of racecars, call start of race and end of race, feed race results into team statistics (external system)
public class Judge extends Vehicle {
    public Team[] getTeamList() {
        return teamList;
    }

    public void setTeamList(Team[] teamList) {
        this.teamList = teamList;
    }

    protected Team[] teamList;
    protected int lapCount; // threshold for win condition
    protected boolean raceRunning;

    public Judge(Team[] teamList){
        this.primaryColour = new Color(15,15,15);
        this.secondaryColour = new Color(200,200,200);
        this.currentCoordinates = new double[]{100,100};
        this.facingAngleRad = 1;
        this.dimensions = new int[]{40,40};
        this.teamList = teamList;
    }

    public void checkWinCondition(Racecar[] participantList){
        for (int i = 0; i < participantList.length; i++){
            if (participantList[i].getLapCount() >= lapCount){
                raceRunning = false;
                int identifier = participantList[i].getTeamIdentifier();
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
