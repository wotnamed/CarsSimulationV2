import java.awt.*;

public class Team {
    // variables
    protected Color primaryColour;
    protected Color secondaryColor;
    protected String teamName;
    protected String teamMotto;
    protected int rating; // winrate for simplicity's sake, 1 = 100%, 0 = 0%
    protected int[] raceStatistics;
    protected int identifier;
    // getters
    public int getRating() {
        return rating;
    }
    public String getTeamMotto() {
        return teamMotto;
    }
    public void setTeamMotto(String teamMotto) {
        this.teamMotto = teamMotto;
    }
    public String getTeamName() {
        return teamName;
    }
    public Color getSecondaryColor() {
        return secondaryColor;
    }
    public Color getPrimaryColour() {
        return primaryColour;
    }
    // setters
    public int getIdentifier() {
        return identifier;
    }
    public int[] getRaceStatistics() {
        return raceStatistics;
    }
    public void setRaceStatistics(int[] raceStatistics) {
        this.raceStatistics = raceStatistics;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setPrimaryColour(Color primaryColour) {
        this.primaryColour = primaryColour;
    }
    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    // init
    public Team(Color primaryColour, Color secondaryColor, String teamName, String teamMotto, int rating, int identifier, int[] raceStatistics){
        this.primaryColour = primaryColour;
        this.secondaryColor = secondaryColor;
        this.teamName = teamName;
        this.teamMotto = teamMotto;
        this.rating = rating;
        this.identifier = identifier;
        this.raceStatistics = raceStatistics; // won / lost
    }
}


