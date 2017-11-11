package lexicalAnalyser.analyzer.DFA.state;

import lexicalAnalyser.analyzer.DFA.TerminalType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public class State4 implements State {

    public static int STATE_INDEX = 4;
    private static final Map<String, TerminalType> symbols;
    private String lexeme;

    static {
        symbols = new HashMap<>();
        symbols.put(TerminalType.ADD.getValue(), TerminalType.ADD);
        symbols.put(TerminalType.MINUS.getValue(), TerminalType.MINUS);
        symbols.put(TerminalType.MULTIPLY.getValue(), TerminalType.MULTIPLY);
        symbols.put(TerminalType.DIVIDE.getValue(), TerminalType.DIVIDE);
        symbols.put(TerminalType.L_BRACKET.getValue(), TerminalType.L_BRACKET);
        symbols.put(TerminalType.R_BRACKET.getValue(), TerminalType.R_BRACKET);
        symbols.put(TerminalType.L_BRACE.getValue(), TerminalType.L_BRACE);
        symbols.put(TerminalType.R_BRACE.getValue(), TerminalType.R_BRACE);
        symbols.put(TerminalType.SEMICOLON.getValue(), TerminalType.SEMICOLON);
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
    public TerminalType getTokenType() {
        TerminalType type = symbols.get(lexeme);
        return type;
    }
}
