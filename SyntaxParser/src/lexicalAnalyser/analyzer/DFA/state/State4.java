package lexicalAnalyser.analyzer.DFA.state;

import analyzer.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public class State4 implements State {

    public static int STATE_INDEX = 4;
    private static final Map<String, Token.TokenType> symbols;
    private String lexeme;

    static {
        symbols = new HashMap<>();
        symbols.put(Token.TokenType.ADD.getValue(), Token.TokenType.ADD);
        symbols.put(Token.TokenType.MINUS.getValue(), Token.TokenType.MINUS);
        symbols.put(Token.TokenType.MULTIPLY.getValue(), Token.TokenType.MULTIPLY);
        symbols.put(Token.TokenType.DIVID.getValue(), Token.TokenType.DIVID);
        symbols.put(Token.TokenType.L_BRACKET.getValue(), Token.TokenType.L_BRACKET);
        symbols.put(Token.TokenType.R_BRACKET.getValue(), Token.TokenType.R_BRACKET);
        symbols.put(Token.TokenType.L_BRACE.getValue(), Token.TokenType.L_BRACE);
        symbols.put(Token.TokenType.R_BRACE.getValue(), Token.TokenType.R_BRACE);
        symbols.put(Token.TokenType.SEMICOLON.getValue(), Token.TokenType.SEMICOLON);
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
        Token.TokenType type = symbols.get(lexeme);
        return type;
    }
}
