package com.example.calculatorprojectlsi;

import android.os.Bundle;
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

    private static String equation = "";
    private static final String regexNumber = "\\d+(\\.\\d+)?";
    private static final String regexOP = "[*+/\\-]";

    private TextView prevResult;
    private EditText display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        prevResult = findViewById(R.id.prevCalculation);
        display = findViewById(R.id.displayEditText);
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
        if (!equation.isEmpty()) {
            equation = "";
            display.setText(equation);
        }
    }

    public void opBtn(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        updateText(buttonText);
    }

    public void equalBtn(View view) {
        try {
            double result = calculate();
            prevResult.setText(String.valueOf(result));
            display.setText(String.valueOf(result));
            equation = "";
        } catch (Exception e) {
            display.setText("Error");
        }
    }

    public double calculate() throws Exception {
        String exp = equation;
        exp = exp.replace(getString(R.string.divideText).charAt(0), '/');
        exp = exp.replace(getString(R.string.multiplyText).charAt(0), '*');
        Queue<String> queue = infixToPostfix(exp);
        return evaluatePostfix(queue);
    }

    public double evaluatePostfix(Queue<String> queue) throws Exception {
        Stack<Double> stack = new Stack<>();
        Pattern patternNumber = Pattern.compile(regexNumber);
        Pattern patternOP = Pattern.compile(regexOP);

        while (!queue.isEmpty()) {
            String var = queue.poll();
            Matcher matcherNumber = patternNumber.matcher(var);
            Matcher matcherOperator = patternOP.matcher(var);

            if (matcherNumber.matches()) {
                stack.push(Double.parseDouble(var));
            } else if (matcherOperator.matches()) {
                double secondNumber = stack.pop();
                double firstNumber = stack.pop();
                double result = applyOperation(firstNumber, secondNumber, var);
                stack.push(result);
            }
        }
        return stack.pop();
    }

    private double applyOperation(double first, double second, String op) throws Exception {
        switch (op) {
            case "*":
                return first * second;
            case "/":
                if (second == 0) {
                    throw new Exception("Divide by zero");
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

        for (String element : tokens) {
            Matcher matcherNumber = patternNumber.matcher(element);
            Matcher matcherOperator = patternOP.matcher(element);

            if (matcherNumber.matches()) {
                queue.add(element);
            } else if (matcherOperator.matches()) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(element)) {
                    queue.add(stack.pop());
                }
                stack.push(element);
            } else if (element.equals("(")) {
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
}