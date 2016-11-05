/**
 * Created by *** on 16-10-29.
 */


import java.util.*;
import java.util.jar.Pack200;

public class Calc {
    private static final int NUM = 0, OP = 1, LB = 2, RB = 3, DK = 4, EMPTY = -1;
    private static final int SUCCEED = 0, NO_NUM = 1, NO_OP = 2, OP_ERROR = 3;
    private int last_type = EMPTY;
    private String cur_str;
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
    private int fetch_str(ListIterator<String> liter){
        cur_str = liter.next();
        if(cur_str.matches("\\d+")){
            return NUM;
        }
        else{
            switch (cur_str){
                case "-":
                    if(LB == last_type || EMPTY == last_type || OP == last_type){
                        // it is a negative sign
                        int ret = fetch_str(liter);
                        if(ret != NUM){
                            // exception, negative sign followed by non-num
                            System.out.println("fetch_str error, negative sign followed by non-num");
                            return DK;
                        }
                        cur_str = "-" + cur_str;
                        return NUM;
                    }
                    else{
                        // it is an operator
                        return OP;
                    }
                case "+":
                case "*":
                case "/":
                case "%":
                    return OP;
                case "(":
                    return LB;
                case ")":
                    return RB;
                default:
                    return DK;
            }

        }
    }
    private boolean is_over(String last_op, String cur_op){
        if(cur_op.equals(")")) return last_op.equals("(");
        else if(cur_op.equals("")) return last_op.equals("");
        else return !op_last_cur_map.get(last_op + cur_op);
    }
    private int eval_till_op(String op){
        String last_op = "";
        if(num_stack.isEmpty()){
            num_stack.push(0.0);
            return NO_NUM;
        }

        //if(op_stack.isEmpty()){
        //    return NO_OP;
        //}

        double b = num_stack.pop();

        while(true){
            if(op_stack.isEmpty())
                last_op = "";
            else
                last_op = op_stack.pop();
            if(is_over(last_op, op)){
                if(!(last_op.equals("(") && op.equals(")")) && !last_op.equals(""))
                    op_stack.push(last_op);
                //op_stack.push(op);
                break;
            }

            if(num_stack.isEmpty()){
                num_stack.push(b);
                break;
            }
            double a = num_stack.pop();
            switch(last_op){
                case "+":
                    b = a + b;
                    break;
                case "-":
                    b = a - b;
                    break;
                case "*":
                    b = a * b;
                    break;
                case "/":
                    b = a / b;
                    break;
                case "%":
                    b = a % b;
                    break;
                default:
                    // exception: unknown op
                    System.out.println("eval error, unknown op");
                    return OP_ERROR;
            }

        }
        num_stack.push(b);
        return SUCCEED;

    }
    public double calculate(String exp){
        parse(exp);
        double res = 0;
        ListIterator<String> liter = exp_list.listIterator();

        while(liter.hasNext()){
            int fetch_ret = fetch_str(liter);
            switch (fetch_ret){
                case NUM:
                    if(EMPTY == last_type || OP == last_type || LB == last_type){
                        // legal syntax
                        num_stack.push(Double.parseDouble(cur_str));
                        break;
                    }
                    else{
                        // exception, RB + NUM || NUM + NUM
                        System.out.println("fetch error, RB + NUM || NUM + NUM");
                        return 0;
                    }
                case OP:
                    if(RB == last_type || NUM == last_type){
                        switch (eval_till_op(cur_str)){
                            case SUCCEED:
                                break;
                            default:
                                System.out.println("calculate error, eval failed");
                                return 0.0;
                        }
                        if(!cur_str.equals(")"))
                            op_stack.push(cur_str);
                        break;
                    }
                    else{
                        // exception, OP + OP || LB + OP || EMPTY + OP
                        System.out.println("fetch error, OP + OP || LB + OP || EMPTY + OP");
                        return 0;
                    }
                case LB:
                    op_stack.push(cur_str);
                    break;
                case RB:
                    switch (eval_till_op(cur_str)){
                        case SUCCEED:
                            break;
                        default:
                            System.out.println("calculate error, eval failed");
                            return 0.0;
                    }
                    break;
                default:
                    // exception, unknown type
                    System.out.println("fetch error, unknown type fetched");
                    return 0;

            }
            last_type = fetch_ret;
        }

        // final evaluation
        if(RB == last_type || NUM == last_type){
            switch (eval_till_op("")){
                case SUCCEED:
                    break;
                default:
                    System.out.println("calculate error, eval failed");
                    return 0.0;
            }
        }
        else{
            // exception, OP + OP || LB + OP || EMPTY + OP
            System.out.println("fetch error, OP + END || LB + END || EMPTY + END");
            return 0;
        }

        return num_stack.pop();
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
