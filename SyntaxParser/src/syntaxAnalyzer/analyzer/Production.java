package syntaxAnalyzer.analyzer;

import lexicalAnalyser.analyzer.DFA.TerminalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Thpffcj on 2017/11/4.
 * 产生式
 */
public class Production {

    NonTerminal left;
    List<Symbol> right;

    public Production(NonTerminal left) {
        this.left = left;
        right = new ArrayList<>();
    }

    public Set<TerminalType> getFirstOfRight(){
        return null;
    }

    public void addSymbol(Symbol symbol) {
        right.add(symbol);
    }
}
