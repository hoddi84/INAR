import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Helper {

    public static ArrayList<StateActionValue> CreateR() {

        ArrayList<StateActionValue> list = new ArrayList<>();

        StateActionValue state1act1 = new StateActionValue(Races.Human, MerchantActions.LetIn, 1.0);
        StateActionValue state1act2 = new StateActionValue(Races.Human, MerchantActions.ThrowOut, 0.0);
        StateActionValue state2act1 = new StateActionValue(Races.Troll, MerchantActions.LetIn, 1.0);
        StateActionValue state2act2 = new StateActionValue(Races.Troll, MerchantActions.ThrowOut, 0.0);
        list.add(state1act1);
        list.add(state1act2);
        list.add(state2act1);
        list.add(state2act2);

        return list;
    }

    public static ArrayList<StateActionValue> CreateQ() {

        ArrayList<StateActionValue> list = new ArrayList<>();
        /*
        StateActionValue state1act1 = new StateActionValue(Races.Human, MerchantActions.LetIn, 5.0);
        StateActionValue state1act2 = new StateActionValue(Races.Human, MerchantActions.ThrowOut, 5.0);
        StateActionValue state2act1 = new StateActionValue(Races.Troll, MerchantActions.LetIn, 5.0);
        StateActionValue state2act2 = new StateActionValue(Races.Troll, MerchantActions.ThrowOut, 5.0);
        list.add(state1act1);
        list.add(state1act2);
        list.add(state2act1);
        list.add(state2act2);
        */
        StateActionValue state1act1 = new StateActionValue(Races.Orc, MerchantActions.LetIn, 6.0);
        StateActionValue state1act2 = new StateActionValue(Races.Troll, MerchantActions.ThrowOut, 3.0);
        list.add(state1act1);
        list.add(state1act2);
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
}
