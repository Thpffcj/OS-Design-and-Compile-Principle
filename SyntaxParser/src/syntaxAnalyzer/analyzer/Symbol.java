package syntaxAnalyzer.analyzer;

import lexicalAnalyser.analyzer.DFA.TerminalType;

import java.util.Set;

/**
 * Created by Thpffcj on 2017/11/5.
 */
public interface Symbol {

    public boolean isTerminal();

    public Set<TerminalType> first();

    public SymbolType getType();

    public String toString();
}
