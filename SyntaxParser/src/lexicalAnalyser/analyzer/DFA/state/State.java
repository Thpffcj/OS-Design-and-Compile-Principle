package lexicalAnalyser.analyzer.DFA.state;


import lexicalAnalyser.analyzer.DFA.TerminalType;
/**
 * Created by Thpffcj on 2017/10/20.
 */
public interface State {

    public int nextState(char c, String lexeme);

    public boolean isEnd();

    public TerminalType getTokenType();
}
