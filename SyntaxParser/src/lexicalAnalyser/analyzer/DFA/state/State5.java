package lexicalAnalyser.analyzer.DFA.state;

import lexicalAnalyser.analyzer.DFA.TerminalType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public class State5 implements State {

    public static int STATE_INDEX = 5;
    private static final Map<String, TerminalType> symbols;
    private String lexeme;

    static {
        symbols = new HashMap<>();
        symbols.put(TerminalType.GREATER.getValue(), TerminalType.GREATER);
        symbols.put(TerminalType.SMALLER.getValue(), TerminalType.SMALLER);
        symbols.put(TerminalType.ASSIGNMENT.getValue(), TerminalType.ASSIGNMENT);
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
    public TerminalType getTokenType() {
        return symbols.get(lexeme);
    }
}
