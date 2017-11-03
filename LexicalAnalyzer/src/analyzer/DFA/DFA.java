package analyzer.DFA;

import analyzer.DFA.state.*;
import analyzer.Table;
import analyzer.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thpffcj on 2017/10/19.
 */
public class DFA {

    private Table varTable;
    private Table constantTable;
    private List<State> states;//表驱动：状态机
    private int pointer;
    private int state;

    public DFA(Table varTable, Table constantTable) {
        this.constantTable = constantTable;
        this.varTable = varTable;
        init();
    }

    private void init(){
        pointer = -1;
        state = -1;
        //将所有状态加入有限状态序列中
        states = new ArrayList<>();
        states.add(new State0());
        states.add(new State1());
        states.add(new State2());
        states.add(new State3());
        states.add(new State4());
        states.add(new State5());
        states.add(new State6());
        states.add(new State7());
    }

    /**
     *
     * @param startPos 下一个词素的首个字符在字符串中的位置
     * @param source 源代码字符串
     * @return 如果能正常识别，则返回一个词法单元，否则返回null
     */
    public Token getToken(int startPos, String source) {

        // 记录词素
        StringBuilder lexeme = new StringBuilder();
        Token token;
        state = 0;  // 状态机的状态
        pointer = startPos;

        do {
            // 读取指针当前所指字符
            char nextChar = source.charAt(pointer);
            // 保存上次的状态
            int lastState = state;
            // 根据自动机获取下次的状态
            state = nextState(nextChar, state, lexeme.toString());
            // 不能识别nextChar
            if (-1 == state) {
                // 上一状态为接受状态
                if (states.get(lastState).isEnd()) {
                    Token.TokenType type = states.get(lastState).getTokenType();
                    // 是ID,查变量表
                    if (Token.TokenType.ID == type) {
                        int posInVarTable = varTable.add(lexeme.toString());
                        token = new Token(type, posInVarTable);
                    } else if (Token.TokenType.CONSTANT == type) {
                        // 是数值常量,查常量表
                        double value = Double.parseDouble(lexeme.toString());
                        int posInConsTable = constantTable.add(value);
                        token = new Token(type, posInConsTable);
                    } else {
                        // 是符号,直接返回词法单元
                        token = new Token(type);
                    }
                } else {
                    token = null;
                }
                break;
            }
            lexeme.append(nextChar);
            pointer ++;
        } while (true);

        return token;
    }

    /**
     * 根据当前状态和输入字符寻找下一个状态
     * @param c 下一个字符
     * @param state 当前状态
     * @param lexeme 当前词素（可能不完整）
     * @return 下一状态编号
     */
    private int nextState(char c, int state, String lexeme) {
        return states.get(state).nextState(c, lexeme);
    }

    public int getPointer() {
        return pointer;
    }

}
