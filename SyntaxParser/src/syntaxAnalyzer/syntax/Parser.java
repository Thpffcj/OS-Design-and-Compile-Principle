package syntaxAnalyzer.syntax;

import lexicalAnalyser.analyzer.DFA.TerminalType;
import lexicalAnalyser.analyzer.Token;
import lexicalAnalyser.io.FileHandler;
import syntaxAnalyzer.analyzer.Grammar;
import syntaxAnalyzer.analyzer.NonTerminalType;
import syntaxAnalyzer.analyzer.Production;
import syntaxAnalyzer.analyzer.SymbolType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Thpffcj on 2017/11/1.
 */
public class Parser {

    FileHandler fileHandler;
    List<Token> tokens;
    Stack<SymbolType> symbolTypes;
    List<Production> productions;
    PPT ppt ;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        tokens.add(new Token(TerminalType.END));
        this.symbolTypes = new Stack<>();
        this.productions = new ArrayList<>();
        this.fileHandler = FileHandler.getInstance();
        this.ppt = new PPT(new Grammar());
        initStack();
    }

    private void initStack() {
        symbolTypes.push(TerminalType.END);
        symbolTypes.push(NonTerminalType.START);
    }
}
