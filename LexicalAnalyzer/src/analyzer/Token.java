package analyzer;

/**
 * Created by Thpffcj on 2017/10/19.
 */
public class Token {

    public enum TokenType{
        ID("ID"),
        CONSTANT("CONSTANT"),
        //保留字
        IF("if"),ELSE("else"),WHILE("while"),FOR("for"),
        //符号 +  -  *  /  ;  (  )  {  }  =  >  <  <= >= == !=
        ADD("+"),MINUS("-"),MULTIPLY("*"),DIVID("*"),SEMICOLON(";"),L_BRACKET("("),
        R_BRACKET(")"),L_BRACE("{"),R_BRACE("}"),ASSIGNMENT("="),GREATER(">"),SMALLER("<"),
        GREATER_OR_E(">="),SMALLER_OR_E("<="),EQUAL("=="),NOT_EQUAL("!=");
        private String value;

        public String getValue(){
            return value;
        }

        TokenType(String value){
            this.value = value;
        }
    }
}
