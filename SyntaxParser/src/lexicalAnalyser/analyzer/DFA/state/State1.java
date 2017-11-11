package lexicalAnalyser.analyzer.DFA.state;



import lexicalAnalyser.analyzer.DFA.TerminalType;
import lexicalAnalyser.analyzer.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/10/20.
 */
public class State1 implements State {

    public static int STATE_INDEX = 1;
    private static final Map<String, TerminalType> reserved;
    private String lexeme;

    static {
        reserved = new HashMap<>();
        reserved.put(TerminalType.IF.getValue(), TerminalType.IF);
        reserved.put(TerminalType.ELSE.getValue(), TerminalType.ELSE);
        reserved.put(TerminalType.FOR.getValue(), TerminalType.FOR);
        reserved.put(TerminalType.WHILE.getValue(), TerminalType.WHILE);
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
    public TerminalType getTokenType() {
        TerminalType type = reserved.get(lexeme);
        if (null != type) {
            return type;
        } else {
            return TerminalType.ID;
        }
    }
}
