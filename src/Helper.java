import java.lang.reflect.Array;
import java.time.chrono.MinguoChronology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hoddi84 on 15.11.2016.
 */
public class Helper {

    // not using this now, we are using getRfromPlayer in Merchant.java
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

        list.add(state1act1);
        list.add(state1act2);
        list.add(state2act1);
        list.add(state2act2);
        return list;
    }

    // check if Q contains this player, by seeing if it contains ALL his features.
    // moved this to Merchant.java where it is being used.
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

    // DOING THIS HERE NOW. NOT COMPLETE
    // update the Q valeus when we meet another player w.r.t. his Q score difference.
    // e.g. we update the whole Q table except for the Q values for the race of player we just met,
    // since our playr should have his values updated by the updateQ() method.
    public static void UpdateQAllTable(MerchantActions merchantActions, Merchant merchant, Player player, double value) {
        HashMap<String, Integer> listPartialMatches = new HashMap<>(QPartialPlayerMatches(merchant,player));
        System.out.println("listPartialMatches: " + listPartialMatches);
        // find common attributes to player
        for (int i = 0; i < merchant.Q.size(); i++) {
            double accAttr = 0.0;
            double score = 0.0;
            double total = listPartialMatches.get("total");
            if (merchant.Q.get(i).merchantActions.equals(merchantActions) && !merchant.Q.get(i).raceType.equals(player.raceType)) {
                System.out.println("Im here");
                ArrayList<String> listMatches = new ArrayList<>(QMatchedAttributes(merchant.Q.get(i), player));
                System.out.println("Here now");
                System.out.println("listMatches: " + listMatches);
                for (int k = 0; k < listMatches.size(); k++) {
                    score = listPartialMatches.get(listMatches.get(k));
                    System.out.println("score: " + score);
                    accAttr += score;
                    System.out.println("accAttr: " + accAttr);
                }
                System.out.println("Q-value before: " + merchant.Q.get(i).value);
                merchant.Q.get(i).value += (accAttr/total)*value;
                System.out.println("Q-value = " + merchant.Q.get(i).value + " += " + "(" + accAttr + "/" + total + ")" + "*" + value);
            }

            if (merchantActions.equals(MerchantActions.ThrowOut)) {
                System.out.println("Im here");
                ArrayList<String> listMatches = new ArrayList<>(QMatchedAttributes(merchant.Q.get(i), player));
                System.out.println("Here now");
                System.out.println("listMatches: " + listMatches);
                for (int k = 0; k < listMatches.size(); k++) {
                    score = listPartialMatches.get(listMatches.get(k));
                    System.out.println("score: " + score);
                    accAttr += score;
                    System.out.println("accAttr: " + accAttr);
                }
                System.out.println("Q-value before: " + merchant.Q.get(i).value);
                merchant.Q.get(i).value += (accAttr/total)*value;
                System.out.println("Q-value = " + merchant.Q.get(i).value + " += " + "(" + accAttr + "/" + total + ")" + "*" + value);
            }
        }
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
        accumulatedScore[0] = accumulatedScore[0]/counter; // merchant action 1.
        accumulatedScore[1] = accumulatedScore[1]/counter2; // merchant action 2.
        return accumulatedScore;
    }

    // find the individual score for the attributes of a approaching player from previously known
    // player attributes from Q.
    public static HashMap<String, Double> CalculateIndividualAttrScore(Merchant merchant, Player player) {
        HashMap<String, Integer> map = new HashMap<>(QPartialPlayerMatches(merchant, player));
        HashMap<String, Double> newMap = new HashMap<>();
        for (int i = 0; i < merchant.Q.size(); i++) {
            double newScore = 0.0;
            for (int k = 0; k < merchant.Q.get(i).features.size(); k++) {
                if (map.containsKey(merchant.Q.get(i).features.get(k))) {
                    newScore = merchant.Q.get(i).value / merchant.Q.get(i).features.size();
                    if (newMap.containsKey(merchant.Q.get(i).features.get(k))) {
                        double oldScore = newMap.get(merchant.Q.get(i).features.get(k));
                        double nextScore = newScore + oldScore;
                        newMap.replace(merchant.Q.get(i).features.get(k), oldScore, nextScore);
                    }
                    else {
                        newMap.put(merchant.Q.get(i).features.get(k), newScore);
                    }

                }
            }
        }
        // iterate the newMap and divide by total attribute occurrences from map.
        Iterator it = newMap.entrySet().iterator();
        for (Map.Entry<String, Double> newMapElement : newMap.entrySet()) {
            for (Map.Entry<String, Integer> mapElement : map.entrySet()) {
                if (newMapElement.getKey().equals(mapElement.getKey())) {
                    double oldScore = newMapElement.getValue();
                    double newScore = oldScore/mapElement.getValue();
                    newMapElement.setValue(newScore);
                }
            }
        }
        // iterate through newMap to find the highest valued attribute
        // and it's corresponding string.
        double highest = Double.MIN_VALUE;
        String highestAttribute = "";
        for (Map.Entry<String, Double> element : newMap.entrySet()) {
            if (element.getValue() > highest) {
                highest = element.getValue();
                highestAttribute = element.getKey();
            }
        }
        System.out.println("String: " + highestAttribute + " " + "Value: " + highest);
        // return only highest valued string for now.
        newMap.clear();
        newMap.put(highestAttribute, highest);
        return newMap;
    }

    // create a list containing all the possible races.
    public static ArrayList<PlayerActionsValue> GetActionList() {
        ArrayList<PlayerActionsValue> list = new ArrayList<>();
        PlayerActionsValue orc = new PlayerActionsValue(RaceType.Orc);
        PlayerActionsValue troll = new PlayerActionsValue(RaceType.Troll);
        PlayerActionsValue elf = new PlayerActionsValue(RaceType.Elf);
        PlayerActionsValue human = new PlayerActionsValue(RaceType.Human);
        PlayerActionsValue goblin = new PlayerActionsValue(RaceType.Goblin);
        list.add(orc);
        list.add(troll);
        list.add(elf);
        list.add(human);
        list.add(goblin);
        return list;
    }
}
