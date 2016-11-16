import java.util.ArrayList;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class StateActionValue {

    Races races;
    Race race;
    ArrayList<String> features;
    MerchantActions merchantActions;
    double value;

    public StateActionValue(Races races, MerchantActions merchantActions, double value) {

        this.race = new Race(races);
        this.features = this.race.features.getFeatures();
        this.races = races;
        this.merchantActions = merchantActions;
        this.value = value;
    }

    @Override
    public String toString() {
        return races + " " + features + " " + merchantActions + " " + value;
    }
}
