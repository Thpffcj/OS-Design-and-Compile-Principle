package syntaxAnalyzer.analyzer;

import lexicalAnalyser.analyzer.DFA.TerminalType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Thpffcj on 2017/11/6.
 * 终结符
 */
public class Terminal implements Symbol {

    TerminalType terminalType;

    public Terminal(TerminalType terminalType){
        this.terminalType = terminalType;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public Set<TerminalType> first() {
        Set<TerminalType> first = new HashSet<>();
        first.add((TerminalType) this.getType());
        return first;
    }

    @Override
    public SymbolType getType() {
        return terminalType;
    }

    @Override
    public String toString(){
        return terminalType.toString();
    }
}
