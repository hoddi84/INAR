import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Helper {

    public static ArrayList<StateActionValue> CreateR() {

        ArrayList<StateActionValue> list = new ArrayList<>();

        StateActionValue state1act1 = new StateActionValue(RaceType.Human, MerchantActions.LetIn, 1.0);
        StateActionValue state1act2 = new StateActionValue(RaceType.Human, MerchantActions.ThrowOut, 0.0);
        StateActionValue state2act1 = new StateActionValue(RaceType.Troll, MerchantActions.LetIn, 1.0);
        StateActionValue state2act2 = new StateActionValue(RaceType.Troll, MerchantActions.ThrowOut, 0.0);
        list.add(state1act1);
        list.add(state1act2);
        list.add(state2act1);
        list.add(state2act2);

        return list;
    }

    public static ArrayList<StateActionValue> CreateQ() {

        ArrayList<StateActionValue> list = new ArrayList<>();

        StateActionValue state1act1 = new StateActionValue(RaceType.Human, MerchantActions.LetIn, 5.0);
        StateActionValue state1act2 = new StateActionValue(RaceType.Human, MerchantActions.ThrowOut, 4.0);
        StateActionValue state2act1 = new StateActionValue(RaceType.Troll, MerchantActions.LetIn, 5.0);
        StateActionValue state2act2 = new StateActionValue(RaceType.Troll, MerchantActions.ThrowOut, 5.0);
        /*
        StateActionValue state1act1 = new StateActionValue(RaceType.Orc, MerchantActions.LetIn, 6.0);
        StateActionValue state1act2 = new StateActionValue(RaceType.Orc, MerchantActions.ThrowOut, 2.0);
        StateActionValue state2act1 = new StateActionValue(RaceType.Troll, MerchantActions.LetIn, 3.0);
        StateActionValue state2act2 = new StateActionValue(RaceType.Troll, MerchantActions.ThrowOut, 4.0);
        */
        list.add(state1act1);
        list.add(state1act2);
        list.add(state2act1);
        list.add(state2act2);
        return list;
    }

    // check if Q contains this player, by seeing if it contains ALL his features.
    public static boolean QContainsPlayer(Merchant merchant, Player player) {

        for (int i = 0; i < merchant.Q.size(); i++) {
            if (player.getFeatures().equals(merchant.Q.get(i).features)) {
                return true;
            }
        }
        return false;
    }

    // Q does not contain this player.
    // Check if Q contains a partial match of attributes and return how many matches and what matches.
    public static HashMap<String, Integer> QPartialPlayerMatches(Merchant merchant, Player player) {
        int preValue = 0;
        int total = 0;
        HashMap<String,Integer> map = new HashMap<>();
        for (StateActionValue el : merchant.Q) {
            for (int i = 0; i < el.features.size(); i++) {
                 for (int k = 0; k < player.features.size(); k++) {
                     if (player.features.get(k).equals(el.features.get(i))) {
                         // check if map contains key [player feature]
                         if (map.containsKey(player.features.get(k))) {
                             preValue = map.get(player.features.get(k));
                             map.replace(player.features.get(k), preValue, preValue+1);
                             total++;
                         }
                         else {
                             map.put(player.features.get(k), 1);
                             total++;
                         }
                     }
                 }
            }
        }
        map.put("total", total);
        return map;
    }

    // compare a player to a StateActionValue attributes and list the attributes they have in common.
    public static ArrayList<String> QMatchedAttributes(StateActionValue stateActionValue, Player player) {
        ArrayList<String> list = new ArrayList<>();
        for (String x : stateActionValue.features) {
            for (String y : player.features) {
                if (y.equals(x)) {
                    list.add(x);
                }
            }
        }
        return list;
    }

    // calculate a score for a new player, from previously observed attributes.
    // using the method we sent to David to calculate a new score for a semi unknown player.
    // WE ARE NOT USING THIS. WE WANT THE SCORE TO REPRESENT EACH MERCHANT ACTION SCORE.
    public static double QPartialPlayerScore(Merchant merchant, Player player) {
        HashMap<String, Integer> map = new HashMap<>(QPartialPlayerMatches(merchant,player));
        double totalAttributes = map.get("total");
        int counter = 0;
        double accumulatedScore = 0.0;
        for (StateActionValue element : merchant.Q) {
            double scoreAttributes = 0;
            ArrayList<String> matches = new ArrayList<>(QMatchedAttributes(element, player));
            if (!matches.isEmpty()) {
                counter++;
            }
            for (String match : matches) {
                scoreAttributes += map.get(match);
            }
            accumulatedScore += (scoreAttributes/totalAttributes)*element.value;
        }
        return accumulatedScore/counter;
    }

    // calculate a score for a new player, from previously observed attributes.
    // here we calculate a separate score for each merchant action.
    // we are hardcoding the merchant actions here.
    public static double[] QPartialPlayerScoreEachAction(Merchant merchant, Player player) {
        HashMap<String, Integer> map = new HashMap<>(QPartialPlayerMatches(merchant,player));
        double totalAttributes = map.get("total");
        int counter = 0;
        int counter2 = 0;
        double[] accumulatedScore = {0.0, 0.0};
        for (StateActionValue element : merchant.Q) {
            if (element.merchantActions.equals(MerchantActions.LetIn)) {
                double scoreAttributes = 0;
                ArrayList<String> matches = new ArrayList<>(QMatchedAttributes(element, player));
                if (!matches.isEmpty()) {
                    counter++;
                }
                for (String match : matches) {
                    scoreAttributes += map.get(match);
                }
                accumulatedScore[0] += (scoreAttributes / totalAttributes) * element.value;
            }
            if (element.merchantActions.equals(MerchantActions.ThrowOut)) {
                double scoreAttributes = 0;
                ArrayList<String> matches = new ArrayList<>(QMatchedAttributes(element, player));
                if (!matches.isEmpty()) {
                    counter2++;
                }
                for (String match : matches) {
                    scoreAttributes += map.get(match);
                }
                accumulatedScore[1] += (scoreAttributes / totalAttributes) * element.value;
            }
        }
        accumulatedScore[0] = accumulatedScore[0]/counter;
        accumulatedScore[1] = accumulatedScore[1]/counter2;
        return accumulatedScore;
    }
}
