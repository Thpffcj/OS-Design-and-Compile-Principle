package analyzer.DFA.state;

import analyzer.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/10/21.
 */
public class State6 implements State {

    public static int STATE_INDEX = 6;
    private static final Map<String, Token.TokenType> symbols;
    private String lexeme;

    static {
        symbols = new HashMap<>();
        symbols.put(Token.TokenType.GREATER_OR_E.getValue(), Token.TokenType.GREATER_OR_E);
        symbols.put(Token.TokenType.SMALLER_OR_E.getValue(), Token.TokenType.SMALLER_OR_E);
        symbols.put(Token.TokenType.NOT_EQUAL.getValue(), Token.TokenType.NOT_EQUAL);
        symbols.put(Token.TokenType.EQUAL.getValue(), Token.TokenType.EQUAL);
    }

    @Override
    public int nextState(char c, String lexeme) {
        this.lexeme = lexeme;
        return -1;
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
