/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Player {

    Races race;
    PlayerActions playerActions;
    int actionScore;

    public Player(Races race, PlayerActions playerActions) {
        this.race = race;
        if (playerActions == PlayerActions.Buy) {
            this.playerActions = playerActions;
            this.actionScore = 2;
        }
        else {
            this.playerActions = playerActions;
            this.actionScore = -3;
        }
    }

    @Override
    public String toString() {
        return race + " " + playerActions + " " + actionScore;
    }
}
