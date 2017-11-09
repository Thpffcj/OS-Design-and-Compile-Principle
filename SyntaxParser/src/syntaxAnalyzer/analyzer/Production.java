package syntaxAnalyzer.analyzer;

import lexicalAnalyser.analyzer.DFA.TerminalType;

import java.util.*;

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

    public List<Symbol> getRight(){
        return right;
    }

    public NonTerminal getLeft(){
        return left;
    }

    public Set<TerminalType> getFirstOfRight() {
        Set<TerminalType> first = new HashSet<>();
        for (Symbol s : right) {
            if (left != null && left.equals(s)) {
                continue;
            }
            Set<TerminalType> f = s.first();
            first.addAll(f);
            if (!f.contains(TerminalType.NULL)) {
                break;
            }
        }
        return first;
    }

    public boolean isEndOfSelf(NonTerminal symbol) {
        Symbol backwardSecond = right.get(right.size() - 2);
        if (left.equals(symbol) && backwardSecond.equals(symbol)){
            return true;
        }else {
            return false;
        }
    }

    public List<Production> after(NonTerminal nonTerminal) {
        List<Production> productions = new ArrayList<>();
        List<Symbol> right2 = new LinkedList<>();
        right2.addAll(right);

        return null;
    }

    public void addSymbol(Symbol symbol) {
        right.add(symbol);
    }
}
