package syntaxAnalyzer.analyzer;

/**
 * Created by Thpffcj on 2017/11/1.
 *
 * START -> MD|T
 * T -> START|~
 * BASE -> FOR|WHILE|IFELSE|SENTENCE;
 * WHILE -> while(CONDITION) BLOCK
 * FOR -> for(SENTENCE;CONDITION;SENTENCE) BLOCK
 * IFELSE -> if(CONDITION) BLOCK else BLOCK
 * BLOCK -> {SENTENCE;}
 * SENTENCE -> id=FACTOR
 * EXPRESSION -> FT2
 * T2-> +FACTOR|*FACTOR|-FACTOR|/FACTOR|~
 * CONDITION -> FT3
 * T3-> ==FACTOR|>FACTOR|<FACTOR|>=FACTOR|<=FACTOR|!=FACTOR
 * FACTOR -> id|const
 */

public enum NonTerminalType implements SymbolType {

    START("Start"),            // 开始符
    FOR("For"),                // for循环
    WHILE("While"),            // while循环
    IFELSE("IfElse"),          // 分支
    BLOCK("Block"),            // 代码块
    SENTENCE("Sentence"),      // 语句
    CONDITION("Condition"),    // 判断条件
    EXPRESSION("Expression"),  // 表达式
    FACTOR("Factor"),          // 元素
    BASE("Base"),              // for|while|Group|IfElse
    TEMP1("T1"),               //临时1
    TEMP2("T2"),               //临时2
    TEMP3("T3");               //临时3

    private String value;

    NonTerminalType(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }

}
