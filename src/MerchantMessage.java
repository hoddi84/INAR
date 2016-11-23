import java.util.HashMap;
import java.util.Map;

/**
 * Created by hordurh15 on 23.11.2016.
 */
public class MerchantMessage {

    private static String[] height = {"Large", "Medium", "Small"};
    private static String[] color = {"Blue", "Green", "Red", "Yellow"};
    private static String[] ears = {"Pointy", "Round", "Cabbage"};

    public static void getMerchantMessage(HashMap<String, Double> map) {

        for (Map.Entry<String, Double> x : map.entrySet()) {
            if (x.getValue() > 0) {
                for (String y : height) {
                    if (y.equals(x.getKey())) {
                        System.out.println("I like " + y.toLowerCase() + " sized people.");
                    }
                }
                for (String y : color) {
                    if (y.equals(x.getKey())) {
                        System.out.println("My favourite people are " + y.toLowerCase() + ". I love " + y.toLowerCase() + " people.");
                    }
                }
                for (String y : ears) {
                    if (y.equals(x.getKey())) {
                        System.out.println("You have really " + y.toLowerCase() + " ears. I like people with " + y.toLowerCase() + " ears.");
                    }
                }
            }
            else if (x.getValue() < 0) {
                for (String y : height) {
                    if (y.equals(x.getKey())) {
                        System.out.println("I don't like " + y.toLowerCase() + " sized people.");
                    }
                }
                for (String y : color) {
                    if (y.equals(x.getKey())) {
                        System.out.println("Ugh you are " + y.toLowerCase() + ". I hate " + y.toLowerCase() + " colored people.");
                    }
                }
                for (String y : ears) {
                    if (y.equals(x.getKey())) {
                        System.out.println("Those are some ugly " + y.toLowerCase() + " ears. I hate people with " + y.toLowerCase() + " ears.");
                    }
                }
            }
            else {
                System.out.println("Hello, friend!");
            }
        }
    }
}
