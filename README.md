# Calculator App

This project is a simple calculator application for Android built using Java and Android Studio. The calculator supports basic arithmetic operations such as addition, subtraction, multiplication, and division, as well as some advanced functions like trigonometric functions (sine, cosine, tangent), natural logarithm, and exponential.

## Features

- Basic arithmetic operations: Addition, Subtraction, Multiplication, Division.
- Advanced functions: Sine, Cosine, Tangent, Natural Logarithm (ln), Exponential (exp).
- Infix to postfix conversion for expression evaluation.
- Support for nested expressions and proper handling of parentheses.

## Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/khalilh2002/Calculator_using_Shunting_yard_algorithm.git
    ```

2. **Open the project in Android Studio:**
    - Launch Android Studio.
    - Select `Open an existing project`.
    - Navigate to the cloned repository and open it.

3. **Build and run the project:**
    - Connect your Android device or start an emulator.
    - Click on the `Run` button in Android Studio.

## Project Structure

- **MainActivity.java**: This is the main activity of the application. It contains the logic for handling button clicks, updating the display, and performing calculations.
- **activity_main.xml**: This layout file defines the user interface of the main activity.
- **strings.xml**: Contains string resources used in the application.
- **color.xml**
- **AndroidManifest.xml**: Describes essential information about the app to the Android build tools, the Android operating system, and Google Play.

## Algorithms

### Infix to Postfix Conversion

The application uses the Shunting Yard algorithm to convert infix expressions (e.g., `3 + 5`) to postfix expressions (e.g., `3 5 +`). This allows for easier evaluation of expressions, especially when handling operator precedence and parentheses.

#### Shunting Yard Algorithm

1. **Initialize two empty data structures**: A stack for operators and a queue for the output.
2. **Tokenize the input string**: Split the input expression into tokens.
3. **Process each token**:
    - If the token is a number, add it to the output queue.
    - If the token is an operator, pop operators from the stack to the output queue until the top of the stack has an operator of lower precedence or the stack is empty, then push the current operator onto the stack.
    - If the token is a left parenthesis `(`, push it onto the stack.
    - If the token is a right parenthesis `)`, pop operators from the stack to the output queue until a left parenthesis is at the top of the stack. Remove the left parenthesis from the stack but do not add it to the output queue.
4. **After processing all tokens**: Pop any remaining operators in the stack to the output queue.

### Postfix Evaluation

The application evaluates the postfix expression using a stack.

#### Postfix Evaluation Algorithm

1. **Initialize an empty stack**.
2. **Process each token** in the postfix expression:
    - If the token is a number, push it onto the stack.
    - If the token is an operator, pop the required number of operands from the stack, perform the operation, and push the result back onto the stack.
    - If the token is a function, pop the argument from the stack, apply the function, and push the result back onto the stack.
3. **The result** of the expression is the value remaining on the stack after all tokens have been processed.

## Code Explanation

### MainActivity.java

The main activity contains methods for handling different button clicks (numbers, operators, functions), updating the display, and performing the calculations. Below are some key methods:

- **updateText(String strToAdd)**: Updates the display and the equation with the provided string.
- **numberBtnPush(View view)**: Handles number button clicks.
- **backSpaceBtn(View view)**: Handles backspace button clicks.
- **clearBtn(View view)**: Clears the current equation.
- **opBtn(View view)**: Handles operator button clicks.
- **functionBtnPush(View view)**: Handles function button clicks, adding the function and an opening parenthesis to the equation.
- **equalBtn(View view)**: Evaluates the current equation and displays the result.

### Calculation Methods

- **calculate()**: Converts the infix expression to postfix and evaluates it.
- **evaluatePostfix(Queue<String> queue)**: Evaluates the postfix expression.
- **applyOperation(String firstS, String secondS, String op)**: Applies an arithmetic operation.
- **applyFunction(String func, double argument)**: Applies a mathematical function.
- **infixToPostfix(String expr)**: Converts an infix expression to postfix using the Shunting Yard algorithm.
- **extractFunctionArgument(Queue<String> queue)**: Extracts the argument for a function from the expression.
- **calculateArgument(String argument)**: Calculates the value of a function argument.

### Utility Methods

- **precedence(String op)**: Determines the precedence of an operator.
- **isFunction(String token)**: Checks if a token is a mathematical function.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
