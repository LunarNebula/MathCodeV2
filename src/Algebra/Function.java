package Algebra;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Function {
    // Stores the value of this Function
    private String value;
    // Stores the list of Functions the Operation operates on
    private List<Function> children;

    /**
     * Creates an empty Function
     */
    public Function() {
        this.value = "";
        this.children = new LinkedList<>();
    }

    /**
     * Initializes and parses a Function
     * @param function the Function as a String
     */
    public Function(String function) {
        this.children = new LinkedList<>();
        try {
            parseAddition(function.replace(" ", ""));
            simplify();
        } catch(NumberFormatException nfe) {
            this.value = "0";
        }
    }

    /**
     * Parses a Function for addition
     * @param function the parsable function
     */
    private void parseAddition(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '+') {
                    try {
                        Function ADDITION = new Function(), SUBTRACTION = new Function();
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
     * Parses a Function for subtraction
     * @param function the parsable function
     */
    private void parseSubtraction(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '-') {
                    try {
                        Function SUBTRACTION = new Function(), MULTIPLICATION = new Function();
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
     * Parses a Function for multiplication
     * @param function the parsable function
     */
    private void parseMultiplication(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '*') {
                    try {
                        Function MULTIPLICATION = new Function(), DIVISION = new Function();
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
     * Parses a Function for division
     * @param function the parsable function
     */
    private void parseDivision(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '/') {
                    try {
                        Function DIVISION = new Function(), NEGATION = new Function();
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
     * Parses a Function for negation
     * @param function the parsable function
     */
    private void parseNegation(String function) {
        if(function.startsWith("-")) {
            Function NEGATION = new Function();
            NEGATION.parseNegation(function.substring(1));
            this.value = "-()";
            this.children.add(NEGATION);
        } else {
            parseExponentiation(function);
        }
    }

    /**
     * Parses a Function for exponentiation
     * @param function the parsable function
     */
    private void parseExponentiation(String function) {
        if(function.length() == 0) {
            throw new NumberFormatException();
        } else {
            for(int i = 0; i < function.length(); i++) {
                if(function.charAt(i) == '^') {
                    try {
                        Function PARENTHESES = new Function(), NEGATION = new Function();
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
     * Parses a Function for parenthetical operations
     * @param function the parsable function
     */
    private void parseParentheses(String function) {
        if(function.endsWith(")")) {
            if(function.startsWith("(")) {
                Function ADDITION = new Function();
                ADDITION.parseAddition(function.substring(1, function.length() - 1));
                this.value = "()";
                this.children.add(ADDITION);
            } else if(function.startsWith("ln(")) {
                Function ADDITION = new Function();
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
     * Simplifies this Function
     */
    private void simplify() {
        for(Function child : this.children) {
            child.simplify();
        }
        switch (this.value) {
            case "+", "*" -> {
                boolean isMultiplication = this.value.equals("*"), isNonzero = true;
                ListIterator<Function> iterator = this.children.listIterator();
                while (iterator.hasNext() && isNonzero) {
                    Function child = iterator.next();
                    if (child.value.equals(this.value)) {
                        iterator.remove();
                        for (Function grandchild : child.children) {
                            iterator.add(grandchild);
                        }
                    } else if(child.value.equals("()") && child.children.get(0).value.equals(this.value)) {
                        iterator.remove();
                        for (Function descendant : child.children.get(0).children) {
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
                Function child = this.children.get(0);
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
                Function child = this.children.get(0);
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
     * Adds this Function to another Function
     * @param addend the addend Function
     * @return this + addend
     */
    public Function add(Function addend) {
        Function sum = new Function();
        sum.value = "+";
        sum.children.add(deepCopy());
        sum.children.add(addend.deepCopy());
        sum.simplify();
        return sum;
    }

    /**
     * Subtracts another Function from this Function
     * @param subtrahend the subtrahend Function
     * @return this - subtrahend
     */
    public Function subtract(Function subtrahend) {
        final Function difference = new Function(), thisDeepCopy = deepCopy(), subtrahendDeepCopy = subtrahend.deepCopy();
        difference.value = "-";
        difference.children.add(thisDeepCopy);
        if(subtrahend.value.equals("+") || subtrahend.value.equals("-")) {
            final Function PARENTHETICAL_TERM = new Function();
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
     * Multiplies this Function by another Function
     * @param multiplicand the multiplicand Function
     * @return this * multiplicand
     */
    public Function multiply(Function multiplicand) {
        final Function product = new Function(), TERM_1 = new Function(), TERM_2 = new Function();
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
     * Divides this Function by another Function
     * @param divisor the divisor Function
     * @return this / divisor
     */
    public Function divide(Function divisor) {
        final Function quotient = new Function(), TERM_1 = new Function(), TERM_2 = new Function();
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
     * Raises this Function to the power of another Function
     * @param pow the exponent Function
     * @return this ^ pow
     */
    public Function pow(Function pow) {
        final Function antilogarithm = new Function(), TERM_1 = new Function(), TERM_2 = new Function();
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
     * Negates this Function
     * @return -this
     */
    public Function negate() {
        final Function negation = new Function(), TERM_1 = new Function();
        negation.value = "-()";
        TERM_1.value = "()";
        TERM_1.children.add(deepCopy());
        negation.children.add(TERM_1);
        negation.simplify();
        return negation;
    }

    /**
     * Takes the natural logarithm of this Function
     * @return ln(this)
     */
    public Function ln() {
        final Function logarithm = new Function();
        logarithm.value = "ln()";
        logarithm.children.add(deepCopy());
        logarithm.simplify();
        return logarithm;
    }

    /**
     * Provides a deep copy of this Function
     * @return an exact replica of this Function
     */
    public Function deepCopy() {
        Function copy = new Function();
        copy.value = this.value;
        for(Function child : this.children) {
            copy.children.add(child.deepCopy());
        }
        return copy;
    }

//    /**
//     * Evaluates the Function at a certain variable
//     * @param x the target variable to be replaced
//     * @param r the replacement value
//     * @return the new Function
//     */
//    public Function evaluate(Function x, Function r) {
//        if (! x.isVariable()) {
//            return null;
//        }
//        ListIterator<Function> iterator = this.children.listIterator();
//        while(iterator.hasNext()) {
//            Function child = iterator.next();
//
//        }
//        return null;
//    }

    /**
     * Finds the partial derivative of this Function with respect to an input variable
     * @param dx the comparator variable for derivation
     * @return the partial derivative of this Function with respect to dx
     * @throws IllegalArgumentException if the target Function is not a variable
     */
    public Function partialDerivative(Function dx) throws IllegalArgumentException {
        if(! dx.isVariable()) {
            throw new IllegalArgumentException();
        }
        Function derivative = new Function("0");
        switch (this.value) {
            case "+" -> {
                for(Function child : this.children) {
                    derivative = derivative.add(child.partialDerivative(dx));
                }
            }
            case "-" -> derivative = this.children.get(0).partialDerivative(dx).subtract(this.children.get(1).partialDerivative(dx));
            case "*" -> {
                Function product = new Function("1");
                for(Function child : this.children) {
                    derivative = derivative.multiply(child).add(child.partialDerivative(dx).multiply(product));
                    product = product.multiply(child);
                }
            }
            case "/" -> {
                Function a = this.children.get(0), b = this.children.get(1);
                derivative = a.partialDerivative(dx).multiply(b).subtract(b.partialDerivative(dx).multiply(a)).divide(b.multiply(b));
            }
            case "^" -> derivative = multiply(this.children.get(1).multiply(this.children.get(0).ln()).partialDerivative(dx));
            case "()" -> derivative = this.children.get(0).partialDerivative(dx);
            case "ln()" -> derivative = this.children.get(0).partialDerivative(dx).divide(this.children.get(0));
            case "-()" -> derivative = this.children.get(0).partialDerivative(dx).negate();
            default -> derivative= new Function(this.value.equals(dx.toString()) ? "1" : "0");
        }
        return derivative;
    }

    /**
     * Determines whether this Function is a constant
     * @return true if this Function is a single numerical constant, else false
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
     * Determines whether this Function is a variable
     * @return true if this Function is a single variable, else false
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
     * Determines whether this Function is equal to another Function
     * @param o the comparator Function
     * @return true if the two Functions are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Function comparator)) {
            return false;
        }
        ListIterator<Function> iterator = comparator.children.listIterator();
        for(Function child : this.children) {
            if(! (iterator.hasNext() || child.equals(iterator.next()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts this Function to a printable format
     * @return this Function as a String
     */
    @Override
    public String toString() {
        String print = "";
        switch (this.value) {
            case "+", "-", "*", "/", "^" -> {
                for (Function child : this.children) {
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
     * Converts this Function to a printable format while preserving its recursive layers
     * @return this Function as a String tree
     */
    public String treeToString() {
        return treeToString("");
    }

    /**
     * Converts this Function to a printable format while preserving its recursive layers
     * @param indent the indent that represents the location of this child Function in the Function tree
     * @return this Function as a tree
     */
    private String treeToString(String indent) {
        String print = indent.concat(this.value);
        indent = indent.concat("\t");
        for(Function child : this.children) {
            print = print.concat("\n").concat(child.treeToString(indent));
        }
        return print;
    }

    /**
     * Prints this Function
     */
    public void print() {
        System.out.println(toString());
    }

    /**
     * Prints this Function as a tree
     */
    public void printTree() {
        System.out.println(treeToString());
    }
}
