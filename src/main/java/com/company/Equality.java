package com.company;

import java.util.Objects;
import java.util.Stack;

public class Equality implements ExpressionEvaluator {

    private String Expression;

    Equality(String str) {
        this.Expression = str;
    }

    @Override
    public void delSpace() {
        StringBuilder newstr = new StringBuilder();
        for (int pos = 0; pos < Expression.length(); pos++)
            if (Expression.charAt(pos) != ' ')
                newstr.append(Expression.charAt(pos));
        Expression = newstr.toString();
    }

    @Override
    public boolean correctness() {
        if (Expression.isEmpty()) return false;
        delSpace();
        int bracket = 0;

        for (int pos = 0; pos < Expression.length(); pos++) {
            if (bracket >= 0) {
                switch (Expression.charAt(pos)) {
                    case '+': case '-': case '*': case '/': {
                        if (pos == 0 || pos == Expression.length() - 1) return false;
                        if (Expression.charAt(pos+1) == '+' || Expression.charAt(pos+1) == '-' || Expression.charAt(pos+1) == '*' || Expression.charAt(pos+1) == '/' || Expression.charAt(pos+1) == ')')
                            return false;
                        break;
                    }

                    case '(': {
                        bracket++;
                        if (Expression.charAt(pos+1) == '+' || Expression.charAt(pos+1) == '-' || Expression.charAt(pos+1) == '*' || Expression.charAt(pos+1) == '/' || Expression.charAt(pos+1) == ')')
                            return false;
                        else if (pos == Expression.length() - 1) return false;
                        break;
                    }

                    case ')': {
                        bracket--;
                        if (pos == 0) return false;
                        if (Expression.charAt(pos-1) == '+' || Expression.charAt(pos-1) == '-' || Expression.charAt(pos-1) == '*' || Expression.charAt(pos-1) == '/' || Expression.charAt(pos-1) == '(' )
                            return false;
                        break;
                    }

                    default:
                        if (Expression.charAt(pos) >= '0' && Expression.charAt(pos) <= '9') {
                            if (pos != 0)
                                if (Expression.charAt(pos-1) == ')')
                                    return false;
                            if (pos != Expression.length()-1)
                                if (Expression.charAt(pos+1) == '(')
                                    return false;
                        } else return false;
                }
            } else return false;
        }
        return bracket == 0;
    }

    @Override
    public boolean postfixNotation() {
        if (!correctness() || Expression.isEmpty())
            return false;

        Stack<Character> charstack = new Stack<>();
        StringBuilder newstr = new StringBuilder();

        for (int pos = 0; pos < Expression.length(); pos++) {
            int typeoper = priority(Expression.charAt(pos));

            if (typeoper == 0) newstr.append(Expression.charAt(pos));

            else if (typeoper == 1) charstack.push(Expression.charAt(pos));

            else if (typeoper > 1) {
                newstr.append(' ');

                while (!charstack.empty()) {
                    if (priority(charstack.peek()) >= typeoper)
                        newstr.append(charstack.pop());
                    else break;
                }

                charstack.push(Expression.charAt(pos));
            }

            else if (typeoper == -1) {
                newstr.append(' ');

                while (priority(charstack.peek()) != 1)
                    newstr.append(charstack.pop());

                charstack.pop();
            }
        }

        while (!charstack.empty()) newstr.append(charstack.pop());
        Expression = newstr.toString();
        return true;
    }

    @Override
    public boolean calculate() {
        boolean t = postfixNotation();
        if (!t) return false;
        else {
            StringBuilder res = new StringBuilder();
            Stack<Double> st = new Stack<>();

            for (int pos = 0; pos < Expression.length(); pos++) {
                if (Expression.charAt(pos) == ' ') continue;

                if (priority(Expression.charAt(pos)) == 0) {
                    while (Expression.charAt(pos) != ' ' && priority(Expression.charAt(pos)) == 0) {
                        res.append(Expression.charAt(pos++));
                        if (pos == Expression.length()) break;
                    }
                    st.push(Double.parseDouble(res.toString()));
                    res = new StringBuilder();
                }

                if (priority(Expression.charAt(pos)) > 1) {
                    double num1 = st.pop();
                    double num2 = st.pop();

                    if (Expression.charAt(pos) == '+')
                        st.push(num2 + num1);
                    if (Expression.charAt(pos) == '-')
                        st.push(num2 - num1);
                    if (Expression.charAt(pos) == '*')
                        st.push(num2 * num1);
                    if (Expression.charAt(pos) == '/')
                        st.push(num2 / num1);
                }
            }
            Expression = Double.toString(st.pop());
            return true;
        }
    }

    @Override
    public String toString() {
        return Expression;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Equality that = (Equality) obj;
        return Objects.equals(Expression, that.Expression);
    }

    private int priority(char ch) {
        if (ch == '*' || ch == '/')
            return 3;
        else if (ch == '+' || ch == '-')
            return 2;
        else if (ch == '(')
            return 1;
        else if (ch == ')')
            return -1;
        return 0;
    }
}
