import java.awt.*;

public class Team {
    protected Color primaryColour;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public Color getPrimaryColour() {
        return primaryColour;
    }

    public void setPrimaryColour(Color primaryColour) {
        this.primaryColour = primaryColour;
    }

    protected Color secondaryColor;
    protected String teamName;
    protected String teamMotto;
    protected int rating; // winrate for simplicity's sake
    protected int[] raceStatistics;

    public int getIdentifier() {
        return identifier;
    }

    protected int identifier;

    public int[] getRaceStatistics() {
        return raceStatistics;
    }

    public void setRaceStatistics(int[] raceStatistics) {
        this.raceStatistics = raceStatistics;
    }

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


