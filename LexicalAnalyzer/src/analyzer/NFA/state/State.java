package analyzer.NFA.state;

import analyzer.Token;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public interface State {

    public int nextState(char c, String lexeme);

    public boolean isEnd();

    public Token.TokenType getTokenType();
}
