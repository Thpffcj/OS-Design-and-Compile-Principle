package analyzer.NFA.state;

import analyzer.Token;

/**
 * Created by Thpffcj on 2017/10/21.
 */
public class State6 implements State {

    public static int STATE_INDEX = 6;

    @Override
    public int nextState(char c, String lexeme) {
        return 0;
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public Token.TokenType getTokenType() {
        return null;
    }
}
