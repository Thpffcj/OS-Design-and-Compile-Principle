package syntaxAnalyzer.syntax;

import lexicalAnalyser.analyzer.DFA.TerminalType;
import syntaxAnalyzer.analyzer.Grammar;
import syntaxAnalyzer.analyzer.NonTerminalType;
import syntaxAnalyzer.analyzer.Production;

import java.util.List;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/11/4.
 */
public class PPT {

    private Map<NonTerminalType, Map<TerminalType, List<Production>>> ppt;

    public PPT(Grammar grammar) {
        ppt = grammar.generatePPT();
    }

    public List<Production> getProduction(NonTerminalType n, TerminalType t) {
        Map<TerminalType,List<Production>> terminals = ppt.get(n);
        List<Production> productions = terminals.get(t);
        return productions;
    }
}
