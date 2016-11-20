/**
 * Created by hoddi84 on 20.11.2016.
 */
public class FeatureCount {

    String feature;
    int buy;
    int sell;
    int steal;
    int leave;

    public FeatureCount(String feature) {
        this.feature = feature;
        buy = 0;
        sell = 0;
        steal = 0;
        leave = 0;
    }

    public String toString() {
        return feature + " buy: " + buy + " sell: " + sell + " steal: " + steal + " leave: " + leave;
    }
}
