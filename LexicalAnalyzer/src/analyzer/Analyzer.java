package analyzer;

import analyzer.NFA.NFA;
import io.FileHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thpffcj on 2017/10/19.
 */
public class Analyzer {

    private String source;
    private Table varTable;
    private Table constantTable;
    private int previousLineNum;
    private List<Token> tokens;
    private int pointer;
    private NFA nfa;

    public Analyzer() {
        FileHandler fileHandler = FileHandler.getInstance();
        varTable = new Table(Table.VARIABLE_TABLE);
        constantTable = new Table(Table.CONSTANT_TABLE);
        tokens = new ArrayList<>();
        nfa = new NFA(varTable, constantTable);
    }

    public void parse() {

        // 扫描字符串
        while (pointer < source.length()) {
            // 将当前指针和字符串传给NFA获取一个词法单元
            Token nextToken = nfa.getToken(pointer, source);
            // 获取到一个合法的词法单元
            if (null != nextToken) {
                tokens.add(nextToken);
                // 更新当前指针位置
                pointer = nfa.getPointer();

            }
        }
    }
}
