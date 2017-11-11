package lexicalAnalyser.analyzer.DFA.state;

import lexicalAnalyser.analyzer.DFA.TerminalType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/10/21.
 */
public class State6 implements State {

    public static int STATE_INDEX = 6;
    private static final Map<String, TerminalType> symbols;
    private String lexeme;

    static {
        symbols = new HashMap<>();
        symbols.put(TerminalType.GREATER_OR_E.getValue(), TerminalType.GREATER_OR_E);
        symbols.put(TerminalType.SMALLER_OR_E.getValue(), TerminalType.SMALLER_OR_E);
        symbols.put(TerminalType.NOT_EQUAL.getValue(), TerminalType.NOT_EQUAL);
        symbols.put(TerminalType.EQUAL.getValue(), TerminalType.EQUAL);
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
        return symbols.get(lexeme);
    }
}
