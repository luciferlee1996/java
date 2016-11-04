/**
 * Created by *** on 16-10-29.
 */


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

            put("+)", true);
            put("-)", true);
            put("*)", true);
            put("/)", true);
            put("%)", true);

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
    private String fetch_num_string(ListIterator<String> liter){
        // tested
        if(!liter.hasNext()){
            // exception
            System.out.println("fetch num error, stack is empty");
            return "0";
        }
        String str = liter.next();
        String sign = new String();
        if(str.equals("(")){
            return str;
        }
        else if(str.equals("-")){
            if(!liter.hasNext()){
                // exception
                System.out.println("except a number behind minus sign.");
            }
            sign = "-";
            str = liter.next();
        }
        if(str.matches("\\d+(\\.\\d+)?")){
            // str is a number
            return sign + str;
        }
        else {
            // exception
            System.out.println("fetch_num_str() gets an illegal string, your input is " + str);
            return "0";
        }
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
                System.out.println("single operation error, your input is " + a + op + b);
                return 0;
        }
    }
    private boolean needs_to_op(String last, String cur){
        if(op_last_cur_map.containsKey(last+cur)){
            return op_last_cur_map.get(last+cur);
        }
        else{
            //exception
            System.out.println("map searching error, your input key is " + last + cur);
            return true;
        }
    }
    private double eval_till_ending_op(String cur_op, double a){
        do{
            if(op_stack.isEmpty()) break;
            String last_op = op_stack.pop();
            if(cur_op.equals(")") && last_op.equals("("))
                break;
            else if(!(needs_to_op(last_op, cur_op) || cur_op.equals(""))){
                op_stack.push(last_op);
                break;
            }
            if(!num_stack.isEmpty()){
                double top = num_stack.pop();
                a = single_operation(top, a, last_op);
                //cur_op = last_op;
            }else{
                // exception
                System.out.println("number of nums error.");
                return 0;
            }

        }while(true);
        if(!cur_op.equals(")"))
            op_stack.push(cur_op);
        return a;
    }
    private double evaluate(ListIterator<String> liter){
        while(liter.hasNext()){
            double a = 0;
            String a_str = fetch_num_string(liter);
            if(a_str.equals("(")){
                op_stack.push(a_str);
                continue;
            }
            else{
                a = Double.parseDouble(a_str);
            }
            if(!liter.hasNext()){
                num_stack.push(eval_till_ending_op("", a));
                break;
            }
            String op = liter.next();
            switch (op){
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                case ")":
                    num_stack.push(eval_till_ending_op(op, a));
                    break;
                case "(":
                    break;
                default:
                    // exception
                    System.out.println("operator error, your input is " + op);
                    return 0;
            }
        }
        if(num_stack.size() != 1){
            Double a = num_stack.pop();
            num_stack.push(eval_till_ending_op("", a));
        }
        return(num_stack.pop());
    }
    public double calculate(String exp){
        parse(exp);
        ListIterator<String> liter = exp_list.listIterator();
        return evaluate(liter);
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("Please enter an mathematical expression,");
            System.out.println("enter \"_END_\" means to terminate:");
            Calc calculator = new Calc();
            String input = scanner.nextLine();
            if(input.equals("")){
                System.out.println(0.0);
                continue;
            }
            if(input.equals("_END_")){
                // terminate command
                System.out.println("Goodbye!");
                break;
            }
            System.out.println("Answer: " + calculator.calculate(input));
        }while(true);
    }
}
