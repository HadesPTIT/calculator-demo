package com.framgia.calculatordemo.util;

public class Calculate {

    public static double eval(final String str) {

        return new Object() {
            int mPos = -1;
            int mCh;
            char mAdd = '+';
            char mSub = '-';
            char mMul = '*';
            char mDiv = '/';
            char mPow = '^';
            char mNum_0 = '0';
            char mNum_9 = '9';
            char mOpenBrac = '(';
            char mCloseBrac = ')';
            char mCharA = 'a';
            char mCharZ = 'z';
            char mCharDot = '.';
            String unexpected = "Unexpected";

            void nextChar() {
                mCh = (++mPos < str.length()) ? str.charAt(mPos) : -1;
            }

            boolean eat(int charToEat) {
                while (mCh == ' ') nextChar();
                if (mCh == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (mPos < str.length()) throw new RuntimeException(unexpected + (char) mCh);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat(mAdd)) x += parseTerm(); // addition
                    else if (eat(mSub)) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat(mMul)) x *= parseFactor(); // multiplication
                    else if (eat(mDiv)) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat(mAdd)) return parseFactor(); // unary plus
                if (eat(mSub)) return -parseFactor(); // unary minus

                double x;
                int startPos = this.mPos;
                if (eat(mOpenBrac)) { // parentheses
                    x = parseExpression();
                    eat(mCloseBrac);
                } else if ((mCh >= mNum_0 && mCh <= mNum_9) || mCh == mCharDot) { // numbers
                    while ((mCh >= mNum_0 && mCh <= mNum_9) || mCh == mCharDot) nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.mPos));
                } else if (mCh >= mCharA && mCh <= mCharZ) { // functions
                    while (mCh >= mCharA && mCh <= mCharZ) nextChar();
                    String func = str.substring(startPos, this.mPos);
                    x = parseFactor();
                } else {
                    throw new RuntimeException(unexpected + (char) mCh);
                }

                if (eat(mPow)) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}

