package lexicalAnalyser.analyzer.DFA.state;


import lexicalAnalyser.analyzer.DFA.TerminalType;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public class State0 implements State{

    public static int STATE_INDEX = 0;

    @Override
    public int nextState(char c, String lexeme) {
        if ((c >= 'a' && c <= 'z') || c >= 'A' && c <= 'Z') {
            return State1.STATE_INDEX;
        } else if (c >= '1' && c <= '9') {
            return State2.STATE_INDEX;
        } else if ('+' == c || '-' == c || '*' == c || '/' == c ||
                   '(' == c || ')' == c || '{' == c || '}' == c ||
                   ';' == c) {
            return State4.STATE_INDEX;
        } else if ('>' == c || '<' == c || '!' == c || '=' == c) {
            return State5.STATE_INDEX;
        } else {
            return -1;
        }
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public TerminalType getTokenType() {
        return null;
    }
}
