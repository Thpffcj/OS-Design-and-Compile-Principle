package syntaxAnalyzer.analyzer;

import lexicalAnalyser.analyzer.DFA.TerminalType;
import sun.misc.FpUtils;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/11/4.
 * 文法
 */
public class Grammar {

    private Map<NonTerminalType,List<Production>> productions;
    private Map<NonTerminalType,Map<TerminalType,List<Production>>> ppt;

    public Grammar(){
        productions = new HashMap<>();
        initGrammar();
    }

    private void initGrammar() {
        NonTerminal start = new NonTerminal(NonTerminalType.START);
        List<Production> factors = initFactor();
    }

    private List<Production> initFactor() {
        NonTerminal factor = new NonTerminal(NonTerminalType.FACTOR);

        Terminal id = new Terminal(TerminalType.ID);
        Production p1 = new Production(factor);
        p1.addSymbol(id);

        Terminal constant = new Terminal(TerminalType.CONSTANT);
        Production p2 = new Production(factor);
        p2.addSymbol(constant);

        List<Production> productions = new ArrayList<>();
        productions.add(p1);
        productions.add(p2);

        factor.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private List<Production> initT2(NonTerminal factor) {
        NonTerminal T2 = new NonTerminal(NonTerminalType.TEMP2);

        Terminal add = new Terminal(TerminalType.ADD);
        Terminal minus = new Terminal(TerminalType.MINUS);
        Terminal multiply = new Terminal(TerminalType.MULTIPLY);
        Terminal divide = new Terminal(TerminalType.DIVIDE);
        Terminal n = new Terminal(TerminalType.NULL);

        Production addition = new Production(T2);
        addition.addSymbol(add);
        addition.addSymbol(factor);

        Production subtraction = new Production(T2);
        subtraction.addSymbol(minus);
        subtraction.addSymbol(factor);

        Production multiplication = new Production(T2);
        multiplication.addSymbol(multiply);
        multiplication.addSymbol(factor);

        Production division = new Production(T2);
        division.addSymbol(divide);
        division.addSymbol(factor);

        Production newFactor = new Production(T2);
        newFactor.addSymbol(n);

        List<Production> productions = new ArrayList<>();
        productions.add(addition);
        productions.add(multiplication);
        productions.add(subtraction);
        productions.add(division);
        productions.add(newFactor);

        T2.setProductions(productions);
        addNull(productions);
        return productions;
    }

    public List<Production> initExpression(NonTerminal factor, NonTerminal T2) {
        NonTerminal expression = new NonTerminal(NonTerminalType.EXPRESSION);

        Production production = new Production(expression);
        production.addSymbol(factor);
        production.addSymbol(T2);

        List<Production> productions = new ArrayList<>();
        productions.add(production);

        expression.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private List<Production> initT3(NonTerminal factor) {
        NonTerminal T3 = new NonTerminal(NonTerminalType.TEMP3);

        Terminal equal = new Terminal(TerminalType.EQUAL);
        Terminal greater = new Terminal(TerminalType.GREATER);
        Terminal smaller = new Terminal(TerminalType.SMALLER);
        Terminal notEqual = new Terminal(TerminalType.NOT_EQUAL);
        Terminal greaterOrEqual = new Terminal(TerminalType.GREATER_OR_E);
        Terminal smallerOrEqual = new Terminal(TerminalType.SMALLER_OR_E);

        Production equalProduction = new Production(T3);
        equalProduction.addSymbol(equal);
        equalProduction.addSymbol(factor);

        Production greaterProduction = new Production(T3);
        greaterProduction.addSymbol(greater);
        greaterProduction.addSymbol(factor);

        Production smallerProduction = new Production(T3);
        smallerProduction.addSymbol(smaller);
        smallerProduction.addSymbol(factor);

        Production notEqualProduction = new Production(T3);
        notEqualProduction.addSymbol(notEqual);
        notEqualProduction.addSymbol(factor);

        Production greaterOrEqualProduction = new Production(T3);
        greaterOrEqualProduction.addSymbol(greaterOrEqual);
        greaterOrEqualProduction.addSymbol(factor);

        Production smallerOrEqualProduction = new Production(T3);
        smallerOrEqualProduction.addSymbol(smallerOrEqual);
        smallerOrEqualProduction.addSymbol(factor);

        List<Production> productions = new ArrayList<>();
        productions.add(equalProduction);
        productions.add(greaterProduction);
        productions.add(smallerProduction);
        productions.add(notEqualProduction);
        productions.add(greaterOrEqualProduction);
        productions.add(smallerOrEqualProduction);

        T3.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private List<Production> initCondition(NonTerminal factor, NonTerminal T3) {
        NonTerminal condition = new NonTerminal(NonTerminalType.CONDITION);

        Production production = new Production(condition);
        production.addSymbol(factor);
        production.addSymbol(T3);

        List<Production> productions = new ArrayList<>();
        productions.add(production);

        condition.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private List<Production> initSentence(NonTerminal expression) {
        NonTerminal sentence = new NonTerminal(NonTerminalType.SENTENCE);

        Terminal id = new Terminal(TerminalType.ID);
        Terminal assignment = new Terminal(TerminalType.ASSIGNMENT);

        Production production = new Production(sentence);
        production.addSymbol(id);
        production.addSymbol(assignment);
        production.addSymbol(expression);

        List<Production> productions = new ArrayList<>();
        production.addSymbol(sentence);

        sentence.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private List<Production> initBlock(NonTerminal start) {
        NonTerminal block = new NonTerminal(NonTerminalType.BLOCK);

        Terminal lBrace = new Terminal(TerminalType.L_BRACE);
        Terminal rBrace = new Terminal(TerminalType.R_BRACE);
        // 分号
        Terminal semicolon = new Terminal(TerminalType.SEMICOLON);

        Production production = new Production(block);
        production.addSymbol(lBrace);
        production.addSymbol(start);
        production.addSymbol(rBrace);

        List<Production> productions = new ArrayList<>();
        production.addSymbol(block);

        block.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private List<Production> initIfElse(NonTerminal condition, NonTerminal block) {
        NonTerminal ifElse = new NonTerminal(NonTerminalType.IFELSE);

        Terminal lBracket = new Terminal(TerminalType.L_BRACKET);
        Terminal rBracket = new Terminal(TerminalType.R_BRACKET);
        Terminal _if = new Terminal(TerminalType.IF);
        Terminal _else = new Terminal(TerminalType.ELSE);

        Production production = new Production(ifElse);
        production.addSymbol(_if);
        production.addSymbol(lBracket);
        production.addSymbol(condition);
        production.addSymbol(rBracket);
        production.addSymbol(block);
        production.addSymbol(_else);
        production.addSymbol(block);

        List<Production> productions = new ArrayList<>();
        productions.add(production);

        ifElse.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private List<Production> initWhileLoop(NonTerminal condition, NonTerminal block) {
        NonTerminal loop = new NonTerminal(NonTerminalType.WHILE);

        Terminal _while = new Terminal(TerminalType.WHILE);
        Terminal lBracket = new Terminal(TerminalType.L_BRACKET);
        Terminal rBracket = new Terminal(TerminalType.R_BRACKET);

        Production production = new Production(loop);
        production.addSymbol(_while);
        production.addSymbol(lBracket);
        production.addSymbol(condition);
        production.addSymbol(rBracket);
        production.addSymbol(block);

        List<Production> productions = new ArrayList<>();
        productions.add(production);

        loop.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private List<Production> initFor(NonTerminal sentence, NonTerminal condition, NonTerminal block) {
        NonTerminal For = new NonTerminal(NonTerminalType.FOR);

        Terminal _for = new Terminal(TerminalType.FOR);
        Terminal lBracket = new Terminal(TerminalType.L_BRACKET);
        Terminal semicolon = new Terminal(TerminalType.SEMICOLON);
        Terminal rBracket = new Terminal(TerminalType.R_BRACKET);

        Production production = new Production(For);
        production.addSymbol(_for);
        production.addSymbol(lBracket);
        production.addSymbol(sentence);
        production.addSymbol(semicolon);
        production.addSymbol(condition);
        production.addSymbol(semicolon);
        production.addSymbol(sentence);
        production.addSymbol(lBracket);
        production.addSymbol(block);

        List<Production> productions = new ArrayList<>();
        productions.add(production);

        For.setProductions(productions);
        addNull(productions);
        return productions;
    }

    private void addNull(List<Production> productions) {
        for (Production p : productions) {
            p.addSymbol(new Terminal(TerminalType.NULL));
        }
    }

    private void initPPT() {
        ppt = new HashMap<>();
        TerminalType[] allTerminals = TerminalType.values();
        NonTerminalType[] allNonTerminals = NonTerminalType.values();
        for (NonTerminalType nonTerminalType : allNonTerminals) {
            Map<TerminalType, List<Production>> terminals = new HashMap<>();
            for (TerminalType terminalType : allTerminals) {
                List<Production> productions = new ArrayList<>();
                terminals.put(terminalType, productions);
            }
            ppt.put(nonTerminalType, terminals);
        }
    }



    public Map<NonTerminalType,Map<TerminalType,List<Production>>> generatePPT() {
        initPPT();
        return null;
    }
}
