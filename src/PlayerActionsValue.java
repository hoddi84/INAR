import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by hoddi84 on 19.11.2016.
 */
public class PlayerActionsValue {

    RaceType raceType;
    Race race;
    ArrayList<String> features;
    HashMap<PlayerActions, Integer> actionList;
    int totalActions = 0;

    public PlayerActionsValue(RaceType raceType) {
        actionList = new HashMap<>();
        this.raceType = raceType;
        this.race = new Race(raceType);
        this.features = race.features.getFeatures();
        this.actionList.put(PlayerActions.Steal, 0);
        this.actionList.put(PlayerActions.Buy, 0);
        this.actionList.put(PlayerActions.Sell, 0);
        this.actionList.put(PlayerActions.Leave, 0);
    }

    public void UpdateActionValues(PlayerActions playerActions) {
        int oldValue = this.actionList.get(playerActions);
        int newValue = oldValue + 1;
        if (playerActions.equals(PlayerActions.Steal)) {
            this.actionList.replace(playerActions, oldValue, newValue);
        }
        else if (playerActions.equals(PlayerActions.Buy)) {
            this.actionList.replace(playerActions, oldValue, newValue);
        }
        else if (playerActions.equals(PlayerActions.Sell)) {
            this.actionList.replace(playerActions, oldValue, newValue);
        }
        else {
            this.actionList.replace(playerActions, oldValue, newValue);
        }
        totalActions++;
    }

    public String toString() {
        return features + " steal: " + this.actionList.get(PlayerActions.Steal) + " buy: " + this.actionList.get(PlayerActions.Buy)
                        + " sell: " + this.actionList.get(PlayerActions.Sell) + " leave: " + this.actionList.get(PlayerActions.Leave);
    }
}
