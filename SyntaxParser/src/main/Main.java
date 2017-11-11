package main;

import lexicalAnalyser.analyzer.Analyzer;
import lexicalAnalyser.analyzer.Token;
import syntaxAnalyzer.syntax.Parser;

import java.util.List;

/**
 * Created by Thpffcj on 2017/10/30.
 */
public class Main {

    public static void main(String[] args) {
        Analyzer lexicalAnalyzer = new Analyzer();
        List<Token> tokens = lexicalAnalyzer.parse();
        Parser syntaxParser = new Parser(tokens);
        syntaxParser.parse();
    }
}
