package lexicalAnalyser.analyzer;

import lexicalAnalyser.analyzer.DFA.DFA;
import lexicalAnalyser.io.FileHandler;

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
    private Scanner scanner;
    private List<Token> tokens;
    private int pointer;
    private DFA dfa;

    public Analyzer() {
        FileHandler fileHandler = FileHandler.getInstance();
        varTable = new Table(Table.VARIABLE_TABLE);
        constantTable = new Table(Table.CONSTANT_TABLE);
        scanner = new Scanner(fileHandler.getSource());
        source = scanner.scan();
        previousLineNum = 1;
        tokens = new ArrayList<>();
        dfa = new DFA(varTable, constantTable);
    }

    public List<Token> parse() {

        // 扫描字符串
        while (pointer < source.length()) {
            // 将当前指针和字符串传给NFA获取一个词法单元
            Token nextToken = dfa.getToken(pointer, source);
            // 获取到一个合法的词法单元
            if (null != nextToken) {
                tokens.add(nextToken);
                // 更新当前指针位置
                pointer = dfa.getPointer();
                // 查看当前字符
                char nextStartChar = source.charAt(pointer);
                if (scanner.isBlankChar(nextStartChar)) {
                    // 是换行符,行号增加
                    if ('\n' == nextStartChar) {
                        previousLineNum ++;
                    }
                    // 跳过当前字符
                    pointer ++;
                }
            } else {  // 获取词法单元失败
                break;
            }
        }
        if (pointer >= source.length()) {
            outputResult();
        } else {
            outputError();
        }
        return tokens;
    }

    private void outputError() {
        FileHandler fileHandler = FileHandler.getInstance();
        fileHandler.outputLexicalError(previousLineNum);
    }

    private void outputResult() {
        FileHandler fileHandler = FileHandler.getInstance();
        fileHandler.outputToken(tokens);
        fileHandler.outputTable(constantTable, FileHandler.CONSTANT_TABLE_FILE_NAME);
        fileHandler.outputTable(varTable, FileHandler.VARIABLE_TABLE_FILE_NAME);
    }
}
