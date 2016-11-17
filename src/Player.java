import java.util.ArrayList;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Player {

    RaceType raceType;
    Race race;
    PlayerActions playerActions;
    int actionScore;
    ArrayList<String> features;

    public Player(RaceType raceType, PlayerActions playerActions) {
        this.raceType = raceType;
        this.race = new Race(raceType);
        this.features = this.race.features.getFeatures();
        if (playerActions == PlayerActions.Buy) {
            this.playerActions = playerActions;
            this.actionScore = 2;
        }
        if (playerActions == PlayerActions.Steal) {
            this.playerActions = playerActions;
            this.actionScore = -3;
        }
        if (playerActions == PlayerActions.Sell) {
            this.playerActions = playerActions;
            this.actionScore = 1;
        }
        if (playerActions == PlayerActions.Leave) {
            this.playerActions = playerActions;
            this.actionScore = 0;
        }
    }

    public ArrayList<String> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return raceType + " " + features + " " + playerActions + " " + actionScore;
    }
}
