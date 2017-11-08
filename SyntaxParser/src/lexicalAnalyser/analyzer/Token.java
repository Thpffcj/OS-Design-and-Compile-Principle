package lexicalAnalyser.analyzer;

import lexicalAnalyser.analyzer.DFA.TerminalType;

/**
 * Created by Thpffcj on 2017/10/19.
 */
public class Token {

    private TerminalType tokenType;
    private int pointerToTable;

    public Token(TerminalType terminalType) {
        this.tokenType = terminalType;
        pointerToTable = -1;
    }

    public Token(TerminalType terminalType, int pointerToTable) {
        this.tokenType = terminalType;
        this.pointerToTable = pointerToTable;
    }

    public TerminalType getTokenType() {
        return tokenType;
    }

    public String toString(){
        String out;
        if (pointerToTable != -1){
            out = "<" + tokenType.name() + "," + pointerToTable + ">";
        }else {
            out = "<" + tokenType.name() + ">";
        }
        return out;
    }
}
