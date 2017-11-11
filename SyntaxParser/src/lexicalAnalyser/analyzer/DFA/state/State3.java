package lexicalAnalyser.analyzer.DFA.state;

import lexicalAnalyser.analyzer.DFA.TerminalType;

/**
 * Created by Thpffcj on 2017/10/21.
 */
public class State3 implements State {

    public static int STATE_INDEX = 3;

    @Override
    public int nextState(char c, String lexeme) {
        if (c >= '0' && c <= '9') {
            return State7.STATE_INDEX;
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
