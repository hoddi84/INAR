import java.util.ArrayList;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class StateActionValue {

    RaceType raceType;
    Race race;
    ArrayList<String> features;
    MerchantActions merchantActions;
    double value;

    public StateActionValue(RaceType raceType, MerchantActions merchantActions, double value) {

        this.race = new Race(raceType);
        this.features = this.race.features.getFeatures();
        this.raceType = raceType;
        this.merchantActions = merchantActions;
        this.value = value;
    }

    @Override
    public String toString() {
        return raceType + " " + features + " " + merchantActions + " " + value;
    }
}
