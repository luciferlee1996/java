/**
 * Created by luc on 16-10-29.
 */
import java.util.*;

public class Calc {

    public static void main(String[] args){
        //System.out.println("Hello, world");
        Scanner scan = new Scanner(System.in);
        //System.out.println(scan.hasNextInt());
        String test = "100 + 2 *(3 / 7)";
        String arr[] = test.split("\\s|(?<=[\\+\\-\\*\\/%()])(?=[^\\s])|(?<=[^\\s])(?=[\\+\\-\\*\\/%()])");
        for (String item: arr) {
            System.out.println(item);
        }
    }
}
