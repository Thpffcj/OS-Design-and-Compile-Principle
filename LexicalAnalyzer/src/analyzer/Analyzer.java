package analyzer;

import io.FileHandler;

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

    public Analyzer() {
        FileHandler fileHandler = FileHandler.getInstance();
        varTable = new Table(Table.VARIABLE_TABLE);
        constantTable = new Table(Table.CONSTANT_TABLE);
    }

    public void parse() {

        while (pointer < source.length()) {

        }
    }
}
