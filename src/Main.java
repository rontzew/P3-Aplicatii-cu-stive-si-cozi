import java.util.*;

class PostfixEvaluator {

    private String expression;
    private String postfix;
    private final Deque<Character> operatorStack;

    private static final HashMap<Character, Integer> operators = new HashMap<>(10);

    private static void createPriority(){
        operators.put('+', 11);
        operators.put('-', 11);
        operators.put('*', 12);
        operators.put('/', 12);
        operators.put('^', 13);
        operators.put('(', 14);
        operators.put(')', 14);
    }

    public PostfixEvaluator(String expression) { //
        this.expression = expression.trim().replaceAll("\\s+", "");
        this.operatorStack = new LinkedList<>();

        generateNotation();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
        generateNotation();
    }

    private void generateNotation() {
        List<Character> exprAsChars = new ArrayList<>(this.expression.length());
        StringBuilder result = new StringBuilder(this.expression.length());
        for(char ch : this.expression.toCharArray()){
            exprAsChars.add(ch);
        }
        createPriority();

        while(exprAsChars.size()>0){
            Character curr = exprAsChars.get(0);
            if(Character.isDigit(curr)){
                result.append(curr);
                exprAsChars.remove(0);
                continue;
            }else if(curr.equals('+') ||  curr.equals('-') || curr.equals('*') || curr.equals('/') || curr.equals('^')){
                    while (!operatorStack.isEmpty() && !operatorStack.peek().equals('(') &&
                            (operators.get(curr) < operators.get(operatorStack.peek())) ||
                            (Objects.equals(operators.get(curr), operators.get(operatorStack.peek())) &&
                                    !curr.equals('^')))  {
                            result.append(operatorStack.pop());
                    }
                operatorStack.push(curr);
                exprAsChars.remove(0);
                continue;
            }
            if(curr.equals('(')){
                operatorStack.push(curr);
                exprAsChars.remove(0);
                continue;
            }
            if(curr.equals(')')){
                while(!operatorStack.peek().equals('(')) {
                    result.append(operatorStack.pop());
                }
                if (operatorStack.isEmpty()) {
                    System.out.println("Error: wrong parentheses in expression");
                }
                operatorStack.pop();
                exprAsChars.remove(0);
            }
        }

        while (!operatorStack.isEmpty()){
            Character o = operatorStack.peek();
            if(o.equals('(')){
                System.out.println("Error: wrong parentheses in expression");
                break;
            }else {
                result.append(operatorStack.pop());
            }
        }
            this.postfix = result.toString();
    }

    public static int add(int a, int b){
        return a+b;
    }
    public static int subtract(int a, int b){
        return a-b;
    }
    public static int multiply(int a, int b){
        return a*b;
    }
    public static int divide(int a, int b){
        return a/b;
    }
    public static double pow(int a, int b){
        return Math.pow(a , b);
    }

    public static int evaluateNotation(String postfix) {
        postfix = postfix.trim().replaceAll("\\s+", "");
        Deque<Integer> operandStack = new LinkedList<>();
        List<Character> postfixAsChars = new ArrayList<>(postfix.length());
        for(char ch : postfix.toCharArray()){
            postfixAsChars.add(ch);
        }

        while(postfixAsChars.size()>0){
            Character curr = postfixAsChars.get(0);
            if(Character.isDigit(curr)){
                operandStack.push(Integer.parseInt(curr.toString()));
                postfixAsChars.remove(0);
            } else {
                int op1, op2;
                int opRes = 0;
                if (!operandStack.isEmpty()) {
                    op1 = Integer.parseInt(operandStack.pop().toString());
                    if (!operandStack.isEmpty()) {
                        op2 = Integer.parseInt(operandStack.pop().toString());
                        switch (curr) {
                            case '+':
                                opRes = add(op2, op1);
                                break;
                            case '-':
                                opRes = subtract(op2, op1);
                                break;
                            case '*':
                                opRes = multiply(op2, op1);
                                break;
                            case '/':
                                opRes = divide(op2, op1);
                                break;
                            case '^':
                                opRes = (int)pow(op2, op1);
                                break;
                            default:
                                System.out.println("Expresia postfixată este greșită");
                                break;
                        }
                        operandStack.push(opRes);
                    }
                }
                postfixAsChars.remove(0);
            }
        }
        return operandStack.pop();
    }

    public String getPostfix() {
        return postfix;
    }
}

public class Main {
    public static final int POSTFIX_GENERATE = 1;
    public static final int POSTFIX_EVAL = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int exercise = scanner.nextInt();
        scanner.nextLine();
        String expression = scanner.nextLine();

        switch (exercise){
            case POSTFIX_GENERATE:
                PostfixEvaluator e = new PostfixEvaluator(expression);
                System.out.println(e.getPostfix());
                break;

            case POSTFIX_EVAL:
                System.out.println(PostfixEvaluator.evaluateNotation(expression));
                break;
        }
    }
}
