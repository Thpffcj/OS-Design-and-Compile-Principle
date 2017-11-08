package lexicalAnalyser.analyzer.DFA;

import syntaxAnalyzer.analyzer.SymbolType;

/**
 * Created by Thpffcj on 2017/11/1.
 */
public enum TerminalType implements SymbolType {

    ID("id"),
    CONSTANT("constant"),
    //保留字
    IF("if"), ELSE("else"), WHILE("while"), FOR("for"),
    //符号 +  -  *  /  ;  (  )  {  }  =  >  <  <= >= == !=
    ADD("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/"), SEMICOLON(";"), L_BRACKET("("),
    R_BRACKET(")"), L_BRACE("{"), R_BRACE("}"), ASSIGNMENT("="), GREATER(">"), SMALLER("<"),
    GREATER_OR_E(">="), SMALLER_OR_E("<="), EQUAL("=="), NOT_EQUAL("!="),

    NULL("~"),END("$");

    private String value;

    TerminalType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString(){
        return value;
    }
}
