package analyzer.NFA.state;

import analyzer.Token;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public class State5 implements State {

    public static int STATE_INDEX = 5;

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
