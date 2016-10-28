/**
 * Created by luc on 16-10-27.
 */

import java.util.Scanner; // for reading in
import java.util.*;
public class HW2_0_1 {
    public static Scanner scanner = new Scanner(System.in);
    private static Map<String, String> map = new HashMap<String, String>();
    private static String[] word_arr;
    private static String ans_str = "Sorry, I cannot understand your question.";

    /* for test: */
    public static void read_map_cmd(){
        System.out.println("Please fill the map, end with key <= \"_END_\":");
        while(read_map_cmd_one_pair());
    }
    private static boolean read_map_cmd_one_pair() {
        if(!scanner.hasNext())
            return false;
        String key = scanner.next();
        if (key.equals("_END_"))
            return false;
        String val = scanner.next();
        map.put(key, val);
        return true;
    }
    public static void test_fill_map(){
        map.put("not", "Please try to restart it.");
        map.put("doesn't", "Please try to restart it.");
        map.put("don't", "Please try to restart it.");
    }

    /**/


    public static void print_map(){
        System.out.println("Map:");
        System.out.println(map);
    }
    public static void read_q() {
        System.out.println("What is your question?\n\tPleast type it within one line:");
        String input = scanner.nextLine();
        word_arr = input.split(" ");
    }
    private static void get_ans(){
        for(String word: word_arr){
            if(map.containsKey(word)){
                ans_str = map.get(word);
                break;
            }
        }
    }
    private static void print_ans(){
        System.out.println(ans_str);
    }
    public static void answer(){
        get_ans();
        System.out.println("Here is the solution:");
        print_ans();
    }
    public static void main(String[] args){
        test_fill_map();

        System.out.println("The default map is:");
        print_map();
        System.out.println("When multiple key words are matched, I only answer the first one and the break;");

        //read_map_cmd();
        //print_map();
        read_q();
        answer();
    }
}
