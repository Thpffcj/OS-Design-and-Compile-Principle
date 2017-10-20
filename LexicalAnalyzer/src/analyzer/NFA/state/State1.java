package analyzer.NFA.state;

import analyzer.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public class State1 implements State {

    public static int STATE_INDEX = 1;
    private static final Map<String, Token.TokenType> reserved;
    private String lexeme;

    static {
        reserved = new HashMap<>();
        reserved.put(Token.TokenType.IF.getValue(), Token.TokenType.IF);
        reserved.put(Token.TokenType.ELSE.getValue(), Token.TokenType.ELSE);
        reserved.put(Token.TokenType.FOR.getValue(), Token.TokenType.FOR);
        reserved.put(Token.TokenType.WHILE.getValue(), Token.TokenType.WHILE);
    }

    @Override
    public int nextState(char c, String lexeme) {
        this.lexeme = lexeme;
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
            return State1.STATE_INDEX;
        } else {
            return -1;
        }
    }

    @Override
    public boolean isEnd() {
        return true;
    }

    @Override
    public Token.TokenType getTokenType() {
        Token.TokenType type = reserved.get(lexeme);
        if (null != type) {
            return type;
        } else {
            return Token.TokenType.ID;
        }
    }
}
