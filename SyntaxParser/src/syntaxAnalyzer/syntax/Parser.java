package syntaxAnalyzer.syntax;

import lexicalAnalyser.analyzer.DFA.TerminalType;
import lexicalAnalyser.analyzer.Token;
import lexicalAnalyser.io.FileHandler;
import syntaxAnalyzer.analyzer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Thpffcj on 2017/11/10.
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

    public void parse() {
        int pointer = 0;
        SymbolType top = symbolTypes.peek();
//        System.out.println(tokens.get(pointer).getTokenType());
//        System.out.println("test...");
        while (top != TerminalType.END) {
            // 匹配
            if (top instanceof TerminalType) {
                if (top.equals(tokens.get(pointer).getTokenType())) {
                    pointer ++;
                    symbolTypes.pop();
                    top = symbolTypes.peek();
                    continue;
                } else {
                    System.out.println(top + " " + tokens.get(pointer).getTokenType());
                    fileHandler.outputSyntaxError();
                    return;
                }
            } else {
                // 输出产生式
                List<Production> pros = ppt.getProduction((NonTerminalType) symbolTypes.peek(), tokens.get(pointer).getTokenType());
                Production pro;
                // 没有歧义
                if (pros.size() == 1){
                    pro = pros.get(0);
                    productions.add(pros.get(0));
                }else {//未找到对应的产生式
                    for (Production p : pros) {
                        System.out.println(p);
                    }
                    fileHandler.outputSyntaxError();
                    return ;
                }
                symbolTypes.pop();
                pushRightIntoStack(pro);
                top = symbolTypes.peek();
            }
        }
        fileHandler.outputProduction(productions);
    }

    private void pushRightIntoStack(Production production) {
        List<Symbol> right = production.getRight();
        for (int i = right.size() - 1; i >= 0; i--){
            Symbol s = right.get(i);
            if ((!s.getType().equals(TerminalType.END))&& (!s.getType().equals(TerminalType.NULL))){
                symbolTypes.push(s.getType());
            }
        }
    }
}
