package alitest;

import java.util.Stack;

public class ValidParenthese {

    public int longestValidParentheses(String s) {

        if(null == s || s.length() == 0) {
            return 0;
        }

        int init = 0;
        Stack<String> characterStack = new Stack<>();
        for(int i = 0; i < s.length(); i++) {

            String c = s.charAt(i) + "";
            if ("{".equals(c)) {
                characterStack.push(c);
            } else {
                if(!characterStack.isEmpty() && "{".equals(characterStack.peek())) {
                    init++;
                    characterStack.pop();
                }
            }
        }
        return init;

    }

    public static void main(String[] args) {
        ValidParenthese validParenthese = new ValidParenthese();
        System.out.print(validParenthese.longestValidParentheses("{}}}{}"));
    }
}
