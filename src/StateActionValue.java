/**
 * Created by hoddi84 on 15.11.2016.
 */
public class StateActionValue {

    Races playerRace;
    MerchantActions merchantActions;
    double value;

    public StateActionValue(Races playerRace, MerchantActions merchantActions, double value) {

        this.playerRace = playerRace;
        this.merchantActions = merchantActions;
        this.value = value;
    }

    @Override
    public String toString() {
        return playerRace + " " + merchantActions + " " + value;
    }
}
