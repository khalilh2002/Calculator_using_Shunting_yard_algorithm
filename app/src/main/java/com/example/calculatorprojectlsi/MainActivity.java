package com.example.calculatorprojectlsi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Calculator";

    private static String equation = "";
    private static final String regexNumber = "\\d+(\\.\\d+)?";
    private static final String regexOP = "[*+/\\-]";
    private static final String regexFunc = "(sin|cos|tan|ln|exp)";

    private TextView prevResult;
    private EditText display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        prevResult = findViewById(R.id.prevCalculation);
        display = findViewById(R.id.displayEditText);

        findViewById(R.id.buttonCos).setOnClickListener(this::functionBtnPush);
        findViewById(R.id.buttonSin).setOnClickListener(this::functionBtnPush);
        findViewById(R.id.buttonTan).setOnClickListener(this::functionBtnPush);
        findViewById(R.id.buttonLn).setOnClickListener(this::functionBtnPush);
        findViewById(R.id.buttonExp).setOnClickListener(this::functionBtnPush);
    }

    private void updateText(String strToAdd) {
        equation += strToAdd;
        display.setText(equation);
    }

    public void numberBtnPush(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        updateText(buttonText);
    }

    public void backSpaceBtn(View view) {
        if (!equation.isEmpty()) {
            equation = equation.substring(0, equation.length() - 1);
            display.setText(equation);
        }
    }

    public void clearBtn(View view) {
        equation = "";
        display.setText(equation);

    }

    public void opBtn(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        updateText(buttonText);
    }



    public void functionBtnPush(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        updateText(buttonText + "(");
    }

    public void equalBtn(View view) {
        try {
            double result = calculate();
            prevResult.setText(String.valueOf(result));
            display.setText(String.valueOf(result));
            equation = "";
        } catch (ArithmeticException e) {
            Log.e(TAG, "Calculation zeor", e);
            display.setText("Divide by zero");
        }catch (Exception e){
            Log.e(TAG, "Calculation error", e);
            display.setText("other");

        }
    }


    public double calculate() throws Exception {
        String exp = equation;
        exp = exp.replace(getString(R.string.divideText).charAt(0), '/');
        exp = exp.replace(getString(R.string.multiplyText).charAt(0), '*');
        Log.d(TAG, "Expression to calculate: " + exp);

        Queue<String> queue = infixToPostfix(exp);
        Log.d(TAG, "Postfix expression: " + queue.toString());

        return evaluatePostfix(queue);
    }

    public double evaluatePostfix(Queue<String> queue) throws Exception {
        Stack<String> stack = new Stack<String>();
        Pattern patternNumber = Pattern.compile(regexNumber);
        Pattern patternOP = Pattern.compile(regexOP);
        Pattern patternFunc = Pattern.compile(regexFunc);


        while (!queue.isEmpty()) {
            String var = queue.poll();
            Log.d(TAG, "Evaluating token: " + var);

            Matcher matcherNumber = patternNumber.matcher(var);
            Matcher matcherOperator = patternOP.matcher(var);
            Matcher matcherFunction = patternFunc.matcher(var);

            if (matcherNumber.matches()) {
                stack.push(var);
                Log.d(TAG,var+"is pushed to stack");
            } else if (matcherOperator.matches()) {
                String secondNumber = stack.pop();
                String firstNumber = stack.pop();
                double result = applyOperation(firstNumber, secondNumber, var);
                stack.push(String.valueOf(result));
            }else if (matcherFunction.matches()) {
//                String function = var;
//                String argument = extractFunctionArgument(queue);
//                Log.d(TAG,"argument of stack function   "+argument);
//                double argValue = calculateArgument(argument);
//                double result = applyFunction(function, argValue);
//                stack.push(result);
                String num1 = stack.pop();
                double result = applyFunction(var , Double.parseDouble(num1));
                stack.push(String.valueOf(result));
            }
        }
        return Double.parseDouble(stack.pop() );
    }

    private String extractFunctionArgument(Queue<String> queue) throws Exception {
        int openParens = 0;
        StringBuilder argument = new StringBuilder();
        Log.e(TAG , "the quueu "+queue.peek());
        boolean insideParens = false;

        while (!queue.isEmpty()) {
            String var = queue.poll();

            if (var.equals("(")) {
                if (insideParens) {
                    openParens++;
                    argument.append(var);
                } else {
                    insideParens = true;
                }
            } else if (var.equals(")")) {
                if (openParens == 0) {
                    break;
                } else {
                    openParens--;
                    argument.append(var);
                }
            } else {
                if (insideParens) {
                    argument.append(var);
                }
            }
        }

        // Debugging output
        Log.d("Calculator", "Extracted argument: " + argument.toString());

        return argument.toString();
    }


    private double calculateArgument(String argument) throws Exception {
        Log.d(TAG, "Calculating argument: " + argument);

        Queue<String> queue = infixToPostfix(argument);
        return evaluatePostfix(queue);
    }



    private double applyOperation(String firstS, String secondS, String op) throws Exception {
        double first = Double.parseDouble(firstS);
        double second = Double.parseDouble(secondS);

        switch (op) {
            case "*":
                return first * second;
            case "/":
                if (second == 0) {
                    throw new ArithmeticException("Divide by zero");
                }
                return first / second;
            case "-":
                return first - second;
            case "+":
                return first + second;
            default:
                throw new IllegalArgumentException("Invalid operator: " + op);
        }
    }

    public Queue<String> infixToPostfix(String expr) {
        Stack<String> stack = new Stack<>();
        Queue<String> queue = new LinkedList<>();
        String[] tokens = expr.split("(?<=[-+*/()])|(?=[-+*/()])");

        Pattern patternNumber = Pattern.compile(regexNumber);
        Pattern patternOP = Pattern.compile(regexOP);
        Pattern patternFunc = Pattern.compile(regexFunc);


        for (String element : tokens) {
            Matcher matcherNumber = patternNumber.matcher(element);
            Matcher matcherOperator = patternOP.matcher(element);
            Matcher matcherFunction = patternFunc.matcher(element);


            if (matcherNumber.matches()) {
                queue.add(element);
            } else if (matcherOperator.matches()) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(element)) {
                    queue.add(stack.pop());
                }
                stack.push(element);
            }else if (matcherFunction.matches()) {
                stack.push(element);
            }else if (element.equals("(")) {
                stack.push(element);
            } else if (element.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    queue.add(stack.pop());
                }
                if (stack.isEmpty() || !stack.peek().equals("(")) {
                    throw new IllegalArgumentException("Mismatched parentheses in expression");
                }
                stack.pop(); // Remove the '(' from the stack
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")) {
                throw new IllegalArgumentException("Mismatched parentheses in expression");
            }
            queue.add(stack.pop());
        }

        return queue;
    }

    private int precedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0; // Should not happen for valid operators
        }
    }
    private boolean isFunction(String token) {
        return token.equals("cos") || token.equals("sin") || token.equals("tan") || token.equals("ln") || token.equals("exp");
    }

    private double applyFunction(String func, double argument) throws Exception {
        switch (func) {
            case "sin":
                return Math.sin(Math.toRadians(argument));
            case "cos":
                return Math.cos(Math.toRadians(argument));
            case "tan":
                return Math.tan(Math.toRadians(argument));
            case "ln":
                return Math.log(argument);
            case "exp":
                return Math.exp(argument);
            default:
                throw new IllegalArgumentException("Invalid function: " + func);
        }
    }
}