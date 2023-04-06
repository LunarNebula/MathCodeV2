package Algebra;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class Expression {
    // Stores the value of this Expression
    private String value;
    // Stores the list of Functions the Operation operates on
    private List<Expression> children;

    /**
     * Creates an empty Expression
     */
    public Expression() {
        this.value = "";
        this.children = new LinkedList<>();
    }

    /**
     * Initializes and parses an Expression
     * @param function the Expression as a String
     */
    public Expression(String function) {
        this.children = new LinkedList<>();
        try {
            parseAddition(function.replace(" ", ""));
            simplify();
        } catch(NumberFormatException nfe) {
            this.value = "0";
        }
    }

    /**
     * Parses an Expression for addition
     * @param function the parsable function
     */
    private void parseAddition(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '+') {
                    try {
                        Expression ADDITION = new Expression(), SUBTRACTION = new Expression();
                        ADDITION.parseAddition(function.substring(0, i));
                        SUBTRACTION.parseSubtraction(function.substring(i + 1));
                        this.value = "+";
                        this.children.add(ADDITION);
                        this.children.add(SUBTRACTION);
                        return;
                    } catch(NumberFormatException nfe) {
                        //addition was not properly parsed
                    }
                }
            }
        }
        parseSubtraction(function);
    }

    /**
     * Parses an Expression for subtraction
     * @param function the parsable function
     */
    private void parseSubtraction(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '-') {
                    try {
                        Expression SUBTRACTION = new Expression(), MULTIPLICATION = new Expression();
                        SUBTRACTION.parseSubtraction(function.substring(0, i));
                        MULTIPLICATION.parseMultiplication(function.substring(i + 1));
                        this.value = "-";
                        this.children.add(SUBTRACTION);
                        this.children.add(MULTIPLICATION);
                        return;
                    } catch(NumberFormatException nfe) {
                        //addition was not properly parsed
                    }
                }
            }
        }
        parseMultiplication(function);
    }

    /**
     * Parses an Expression for multiplication
     * @param function the parsable function
     */
    private void parseMultiplication(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '*') {
                    try {
                        Expression MULTIPLICATION = new Expression(), DIVISION = new Expression();
                        MULTIPLICATION.parseMultiplication(function.substring(0, i));
                        DIVISION.parseDivision(function.substring(i + 1));
                        this.value = "*";
                        this.children.add(MULTIPLICATION);
                        this.children.add(DIVISION);
                        return;
                    } catch(NumberFormatException nfe) {
                        //addition was not properly parsed
                    }
                }
            }
        }
        parseDivision(function);
    }

    /**
     * Parses an Expression for division
     * @param function the parsable function
     */
    private void parseDivision(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '/') {
                    try {
                        Expression DIVISION = new Expression(), NEGATION = new Expression();
                        DIVISION.parseDivision(function.substring(0, i));
                        NEGATION.parseNegation(function.substring(i + 1));
                        this.value = "/";
                        this.children.add(DIVISION);
                        this.children.add(NEGATION);
                        return;
                    } catch(NumberFormatException nfe) {
                        //addition was not properly parsed
                    }
                }
            }
        }
        parseNegation(function);
    }

    /**
     * Parses an Expression for negation
     * @param function the parsable function
     */
    private void parseNegation(String function) {
        if(function.startsWith("-")) {
            Expression NEGATION = new Expression();
            NEGATION.parseNegation(function.substring(1));
            this.value = "-()";
            this.children.add(NEGATION);
        } else {
            parseExponentiation(function);
        }
    }

    /**
     * Parses an Expression for exponentiation
     * @param function the parsable function
     */
    private void parseExponentiation(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '^') {
                    try {
                        Expression PARENTHESES = new Expression(), NEGATION = new Expression();
                        PARENTHESES.parseParentheses(function.substring(0, i));
                        NEGATION.parseNegation(function.substring(i + 1));
                        this.value = "^";
                        this.children.add(PARENTHESES);
                        this.children.add(NEGATION);
                        return;
                    } catch(NumberFormatException nfe) {
                        //addition was not properly parsed
                    }
                }
            }
        }
        parseParentheses(function);
    }

    /**
     * Parses an Expression for parenthetical operations
     * @param function the parsable function
     */
    private void parseParentheses(String function) {
        if(function.endsWith(")")) {
            if(function.startsWith("(")) {
                Expression ADDITION = new Expression();
                ADDITION.parseAddition(function.substring(1, function.length() - 1));
                this.value = "()";
                this.children.add(ADDITION);
            } else if(function.startsWith("ln(")) {
                Expression ADDITION = new Expression();
                ADDITION.parseAddition(function.substring(3, function.length() - 1));
                this.value = "ln()";
                this.children.add(ADDITION);
            } else {
                throw new NumberFormatException();
            }
        } else {
            for(char c : function.toCharArray()) {
                if(! (Character.isLetter(c) || Character.isDigit(c))) {
                    throw new NumberFormatException();
                }
            }
            this.value = function;
        }
    }

    /**
     * Simplifies this Expression
     */
    private void simplify() {
        for(Expression child : this.children) {
            child.simplify();
        }
        switch (this.value) {
            case "+", "*" -> {
                boolean isMultiplication = this.value.equals("*"), isNonzero = true;
                ListIterator<Expression> iterator = this.children.listIterator();
                while (iterator.hasNext() && isNonzero) {
                    Expression child = iterator.next();
                    if (child.value.equals(this.value)) {
                        iterator.remove();
                        for (Expression grandchild : child.children) {
                            iterator.add(grandchild);
                        }
                    } else if(child.value.equals("()") && child.children.get(0).value.equals(this.value)) {
                        iterator.remove();
                        for (Expression descendant : child.children.get(0).children) {
                            iterator.add(descendant);
                        }
                    } else if(child.value.equals("0")) {
                        if(isMultiplication) {
                            isNonzero = false;
                        } else {
                            iterator.remove();
                        }
                    } else if(child.value.equals("1") && isMultiplication) {
                        iterator.remove();
                    }
                }
                if(! isNonzero) {
                    this.value = "0";
                    this.children.clear();
                } else if(this.children.size() == 1) {
                    this.value = this.children.get(0).value;
                    this.children = this.children.get(0).children;
                } else if(this.children.size() == 0) {
                    if(isMultiplication) {
                        this.value = "1";
                    } else {
                        this.value = "0";
                    }
                }
            }
            case "^" -> {
                if (this.children.get(0).value.equals("e") && this.children.get(1).value.equals("ln()")) {
                    this.value = this.children.get(1).children.get(0).value;
                    this.children = this.children.get(1).children.get(0).children;
                } else if(this.children.get(1).value.equals("1")) {
                    this.value = this.children.get(0).value;
                    this.children.clear();
                } else if(this.children.get(1).value.equals("0")) {
                    if(this.children.get(0).value.equals("0")) {
                        throw new ArithmeticException();
                    } else {
                        this.value = "1";
                        this.children.clear();
                    }
                } else if(this.children.get(0).value.equals("1")) {
                    this.value = "1";
                    this.children.clear();
                }
            }
            case "()" -> {
                Expression child = this.children.get(0);
                if ("ln()^".contains(child.value) || child.isConstant() || child.isVariable()) {
                    this.value = child.value;
                    this.children = child.children;
                }
            }
            case "-()" -> {
                if(this.children.get(0).value.equals("-()")) {
                    this.value = this.children.get(0).children.get(0).value;
                    this.children = this.children.get(0).children.get(0).children;
                } else if(this.children.get(0).value.equals("0")) {
                    this.value = "0";
                    this.children.clear();
                } else if(this.children.get(0).value.equals("()") && this.children.get(0).children.get(0).value.equals("-()")) {
                    this.value = this.children.get(0).children.get(0).children.get(0).value;
                    this.children = this.children.get(0).children.get(0).children.get(0).children;
                }
            }
            case "ln()" -> {
                Expression child = this.children.get(0);
                switch (child.value) {
                    case "()" -> {
                        child.value = child.children.get(0).value;
                        child.children = child.children.get(0).children;
                    }
                    case "0" -> throw new ArithmeticException("ln(0) is not well-defined.");
                    case "1" -> {
                        this.value = "0";
                        this.children.clear();
                    }
                    case "e" -> {
                        this.value = "1";
                        this.children.clear();
                    }
                }
            }
            case "/" -> {
                if(this.children.get(0).value.equals("0")) {
                    this.value = "0";
                    this.children.clear();
                }
//                else if(this.children.get(0).value.equals("1")) {
//                    this.value = "^";
//                }
            }
            case "-" -> {
                if(this.children.get(1).value.equals("0")) {
                    this.value = this.children.get(0).value;
                    this.children = this.children.get(0).children;
                } else if(this.children.get(0).value.equals("0")) {
                    this.value = "-()";
                    this.children.remove(0);
                }
            }
        }
    }

    /**
     * Adds this Expression to another Expression
     * @param addend the addend Expression
     * @return this + addend
     */
    public Expression add(Expression addend) {
        Expression sum = new Expression();
        sum.value = "+";
        sum.children.add(deepCopy());
        sum.children.add(addend.deepCopy());
        sum.simplify();
        return sum;
    }

    /**
     * Subtracts another Expression from this Expression
     * @param subtrahend the subtrahend Expression
     * @return this - subtrahend
     */
    public Expression subtract(Expression subtrahend) {
        final Expression difference = new Expression(), thisDeepCopy = deepCopy(), subtrahendDeepCopy = subtrahend.deepCopy();
        difference.value = "-";
        difference.children.add(thisDeepCopy);
        if(subtrahend.value.equals("+") || subtrahend.value.equals("-")) {
            final Expression PARENTHETICAL_TERM = new Expression();
            PARENTHETICAL_TERM.value = "()";
            PARENTHETICAL_TERM.children.add(subtrahend);
            difference.children.add(PARENTHETICAL_TERM);
        } else {
            difference.children.add(subtrahendDeepCopy);
        }
        difference.simplify();
        return difference;
    }

    /**
     * Multiplies this Expression by another Expression
     * @param multiplicand the multiplicand Expression
     * @return this * multiplicand
     */
    public Expression multiply(Expression multiplicand) {
        final Expression product = new Expression(), TERM_1 = new Expression(), TERM_2 = new Expression();
        product.value = "*";
        TERM_1.value = "()";
        TERM_2.value = "()";
        TERM_1.children.add(deepCopy());
        TERM_2.children.add(multiplicand.deepCopy());
        product.children.add(TERM_1);
        product.children.add(TERM_2);
        product.simplify();
        return product;
    }

    /**
     * Divides this Expression by another Expression
     * @param divisor the divisor Expression
     * @return this / divisor
     */
    public Expression divide(Expression divisor) {
        final Expression quotient = new Expression(), TERM_1 = new Expression(), TERM_2 = new Expression();
        quotient.value = "/";
        TERM_1.value = "()";
        TERM_2.value = "()";
        TERM_1.children.add(deepCopy());
        TERM_2.children.add(divisor.deepCopy());
        quotient.children.add(TERM_1);
        quotient.children.add(TERM_2);
        quotient.simplify();
        return quotient;
    }

    /**
     * Raises this Expression to the power of another Expression
     * @param pow the exponent Expression
     * @return this ^ pow
     */
    public Expression pow(Expression pow) {
        final Expression antilogarithm = new Expression(), TERM_1 = new Expression(), TERM_2 = new Expression();
        antilogarithm.value = "^";
        TERM_1.value = "()";
        TERM_2.value = "()";
        TERM_1.children.add(deepCopy());
        TERM_2.children.add(pow.deepCopy());
        antilogarithm.children.add(TERM_1);
        antilogarithm.children.add(TERM_2);
        antilogarithm.simplify();
        return antilogarithm;
    }

    /**
     * Negates this Expression
     * @return -this
     */
    public Expression negate() {
        final Expression negation = new Expression(), TERM_1 = new Expression();
        negation.value = "-()";
        TERM_1.value = "()";
        TERM_1.children.add(deepCopy());
        negation.children.add(TERM_1);
        negation.simplify();
        return negation;
    }

    /**
     * Takes the natural logarithm of this Expression
     * @return ln(this)
     */
    public Expression ln() {
        final Expression logarithm = new Expression();
        logarithm.value = "ln()";
        logarithm.children.add(deepCopy());
        logarithm.simplify();
        return logarithm;
    }

    /**
     * Provides a deep copy of this Expression
     * @return an exact replica of this Expression
     */
    public Expression deepCopy() {
        Stack<Expression> thisStack = new Stack<>(), copyStack = new Stack<>();
        Expression copy = new Expression();
        thisStack.push(this);
        copyStack.push(copy);
        while(! thisStack.isEmpty()) {
            final Expression thisCursor = thisStack.pop();
            final Expression copyCursor = copyStack.pop();
            copyCursor.value = thisCursor.value;
            for(Expression child : thisCursor.children) {
                thisStack.push(child);
            }
            for(int i = 0; i < thisCursor.children.size(); i++) {
                final Expression child = new Expression();
                copyCursor.children.add(child);
                copyStack.push(child);
            }
        }
        return copy;
    }

//    /**
//     * Evaluates the Expression at a certain variable
//     * @param x the target variable to be replaced
//     * @param r the replacement value
//     * @return the new Expression
//     */
//    public Expression evaluate(Expression x, Expression r) {
//        if (! x.isVariable()) {
//            return null;
//        }
//        ListIterator<Expression> iterator = this.children.listIterator();
//        while(iterator.hasNext()) {
//            Expression child = iterator.next();
//
//        }
//        return null;
//    }

    /**
     * Finds the partial derivative of this Expression with respect to an input variable
     * @param dx the comparator variable for derivation
     * @return the partial derivative of this Expression with respect to dx
     * @throws IllegalArgumentException if the target Expression is not a variable
     */
    public Expression partialDerivative(Expression dx) throws IllegalArgumentException {
        if(! dx.isVariable()) {
            throw new IllegalArgumentException();
        }
        Expression derivative = new Expression("0");
        switch (this.value) {
            case "+" -> {
                for(Expression child : this.children) {
                    derivative = derivative.add(child.partialDerivative(dx));
                }
            }
            case "-" -> derivative = this.children.get(0).partialDerivative(dx).subtract(this.children.get(1).partialDerivative(dx));
            case "*" -> {
                Expression product = new Expression("1");
                for(Expression child : this.children) {
                    derivative = derivative.multiply(child).add(child.partialDerivative(dx).multiply(product));
                    product = product.multiply(child);
                }
            }
            case "/" -> {
                Expression a = this.children.get(0), b = this.children.get(1);
                derivative = a.partialDerivative(dx).multiply(b).subtract(b.partialDerivative(dx).multiply(a)).divide(b.multiply(b));
            }
            case "^" -> derivative = multiply(this.children.get(1).multiply(this.children.get(0).ln()).partialDerivative(dx));
            case "()" -> derivative = this.children.get(0).partialDerivative(dx);
            case "ln()" -> derivative = this.children.get(0).partialDerivative(dx).divide(this.children.get(0));
            case "-()" -> derivative = this.children.get(0).partialDerivative(dx).negate();
            default -> derivative= new Expression(this.value.equals(dx.toString()) ? "1" : "0");
        }
        return derivative;
    }

    /**
     * Determines whether this Expression is a constant
     * @return true if this Expression is a single numerical constant, else false
     */
    public boolean isConstant() {
        for(char c : this.value.toCharArray()) {
            if(! Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether this Expression is a variable
     * @return true if this Expression is a single variable, else false
     */
    public boolean isVariable() {
        boolean containsLetter = false;
        for(char c : this.value.toCharArray()) {
            if(Character.isLetter(c)) {
                containsLetter = true;
            } else if(! Character.isDigit(c)) {
                return false;
            }
        }
        return containsLetter;
    }

    /**
     * Determines whether this Expression is equal to another Expression
     * @param o the comparator Expression
     * @return true if the two Functions are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Expression comparator)) {
            return false;
        }
        ListIterator<Expression> iterator = comparator.children.listIterator();
        for(Expression child : this.children) {
            if(! (iterator.hasNext() || child.equals(iterator.next()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts this Expression to a printable format
     * @return this Expression as a String
     */
    @Override
    public String toString() {
        String print = "";
        switch (this.value) {
            case "+", "-", "*", "/", "^" -> {
                for (Expression child : this.children) {
                    print = print.concat(this.value).concat(child.toString());
                }
                print = print.substring(1);
            }
            case "()" -> print = "(".concat(this.children.get(0).toString()).concat(")");
            case "ln()" -> print = "ln(".concat(this.children.get(0).toString()).concat(")");
            case "-()" -> print = "-".concat(this.children.get(0).toString());
            default -> print = this.value;
        }
        return print;
    }

    /**
     * Converts this Expression to a printable format while preserving its recursive layers
     * @return this Expression as a String tree
     */
    public String treeToString() {
        return treeToString("");
    }

    /**
     * Converts this Expression to a printable format while preserving its recursive layers
     * @param indent the indent that represents the location of this child Expression in the Expression tree
     * @return this Expression as a tree
     */
    private String treeToString(String indent) {
        String print = indent.concat(this.value);
        indent = indent.concat("\t");
        for(Expression child : this.children) {
            print = print.concat("\n").concat(child.treeToString(indent));
        }
        return print;
    }

    /**
     * Prints this Expression
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Prints this Expression as a tree
     */
    public void printTree() {
        System.out.println(treeToString());
    }
}
