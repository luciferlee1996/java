/**
 * Created by luc on 16-10-29.
 */

// HERE!!
// test case "1+2 * 3 - (-4 / 2)" didn't pass
// remember to debug

import java.util.*;

public class Calc {
    private Stack<Double> num_stack = new Stack<Double>();
    private Stack<String> op_stack = new Stack<String>();
    private Map<String, Boolean> op_last_cur_map = new HashMap<String, Boolean>(){
        {
            put("++", false);
            put("+-", false);
            put("+*", false);
            put("+/", false);
            put("+%", false);

            put("-+", true);
            put("--", true);
            put("-*", false);
            put("-/", false);
            put("-%", false);

            put("*+", true);
            put("*-", true);
            put("**", false);
            put("*/", false);
            put("*%", true);

            put("/+", true);
            put("/-", true);
            put("/*", true);
            put("//", true);
            put("/%", true);

            put("%+", true);
            put("%-", true);
            put("%*", true);
            put("%/", true);
            put("%%", true);

            put("(+", false);
            put("(-", false);
            put("(*", false);
            put("(/", false);
            put("(%", false);

            // "cur" is the very first operator
            put("+", false);
            put("-", false);
            put("*", false);
            put("/", false);
            put("%", false);
        }
    };
    private List<String> exp_list = null;
    private void parse(String exp){
        // separate numbers and operators
        exp_list = Arrays.asList(exp.split("\\s|(?<=[\\+\\-\\*\\/%()])(?=[^\\s])|(?<=[^\\s])(?=[\\+\\-\\*\\/%()])"));
    }
    private double fetch_num(ListIterator<String> liter){
        // tested
        if(!liter.hasNext()){
            // exception
            return 0;
        }
        String str = liter.next();
        double res;
        if(str.equals("-")){
            res = -Double.parseDouble(liter.next());
        }
        else if(str.matches("\\d+(\\.\\d+)?")){
            // str is a number
            res = Double.parseDouble(str);
        }
        else {
            // exception
            //System.out.println("fetch_num() gets an illegal string!");
            res = 0;
        }
        return res;
    }
    private double single_operation(double a, double b, String op){
        switch(op){
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            case "%":
                return a % b;
            default:
                // exception
                return 0;
        }
    }
    private boolean needs_to_op(String last, String cur){
        if(op_last_cur_map.containsKey(last+cur)){
            return op_last_cur_map.get(last+cur);
        }
        else{
            //exception
            return true;
        }
    }
    private double evaluate(ListIterator<String> liter){
        double res = 0;
        String last_op = new String();
        while(liter.hasNext()){
            if(!op_stack.isEmpty())
                last_op = op_stack.pop();
            double a = fetch_num(liter);
            if(!liter.hasNext()){
                // HERE!! reaches the end of the expression
                while(!num_stack.isEmpty()){
                    double top_num = num_stack.pop();
                    a = single_operation(top_num, a, last_op);
                    if(op_stack.isEmpty())
                        break;
                    last_op = op_stack.pop();
                }
                if(!num_stack.isEmpty() || !op_stack.isEmpty()){
                    // exception
                    return 0;
                }
                res = a;
                break;
            }
            String op = liter.next();
            switch (op){
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                    if(needs_to_op(last_op, op)){
                        double top_num = num_stack.pop();
                        a = single_operation(top_num, a, op_stack.pop());
                    }
                    break;
                case "(":
                    break;
                case ")":
                    //num_stack.push(a);
                    do{
                        if(last_op.equals("(")) break;
                        double top_num = num_stack.pop();
                        a = single_operation(top_num, a, last_op);
                    }while(true);
                    break;
                default:
                    // exception
                    return 0;
            }
            num_stack.push(a);
            if(!op.equals(")")){
                op_stack.push(op);
            }
        }
        return res;
    }
    public double calculate(String exp){
        parse(exp);
        ListIterator<String> liter = exp_list.listIterator();
        return evaluate(liter);
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Calc calculator = new Calc();
        do{
            String input = scanner.nextLine();
            if(input.equals("_END_")){
                // terminate command
                System.out.println("Goodbye!");
                break;
            }
            System.out.println(calculator.calculate(input));
        }while(true);
    }
}
