package analyzer.NFA.state;

import analyzer.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public class State5 implements State {

    public static int STATE_INDEX = 5;
    private static final Map<String, Token.TokenType> symbols;
    private String lexeme;

    static {
        symbols = new HashMap<>();
        symbols.put(Token.TokenType.GREATER.getValue(), Token.TokenType.GREATER);
        symbols.put(Token.TokenType.SMALLER.getValue(), Token.TokenType.SMALLER);
        symbols.put(Token.TokenType.ASSIGNMENT.getValue(), Token.TokenType.ASSIGNMENT);
    }

    @Override
    public int nextState(char c, String lexeme) {
        this.lexeme = lexeme;
        if ('=' == c) {
            return State6.STATE_INDEX;
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
        return symbols.get(lexeme);
    }
}
