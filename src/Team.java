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
    protected int rating;

    public Team(Color primaryColour, Color secondaryColor, String teamName, String teamMotto, int rating){
        this.primaryColour = primaryColour;
        this.secondaryColor = secondaryColor;
        this.teamName = teamName;
        this.teamMotto = teamMotto;
        this.rating = rating;
    }
}


