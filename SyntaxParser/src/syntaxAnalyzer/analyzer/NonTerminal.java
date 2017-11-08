package syntaxAnalyzer.analyzer;

import lexicalAnalyser.analyzer.DFA.TerminalType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Thpffcj on 2017/11/5.
 * 非终结符
 */
public class NonTerminal implements Symbol {

    NonTerminalType nonTerminalType;
    List<Production> productions;

    public NonTerminal(NonTerminalType nonTerminalType) {
        this.nonTerminalType = nonTerminalType;
        productions = new ArrayList<>();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public Set<TerminalType> first() {
        Set<TerminalType> first = new HashSet<>();
        for (Production production : productions) {
            first.addAll(production.getFirstOfRight());
        }
        return first;
    }

    @Override
    public SymbolType getType() {
        return nonTerminalType;
    }

    @Override
    public String toString(){
        return nonTerminalType.toString();
    }

    public void setProductions(List<Production> productions){
        this.productions = productions;
    }
}
