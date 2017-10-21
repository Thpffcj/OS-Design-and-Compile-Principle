package analyzer;

/**
 * Created by Thpffcj on 2017/10/21.
 */
public class Scanner {

    private StringBuilder source;

    public Scanner(StringBuilder source){
        this.source = source;
    }

    public String scan() {
        int index = 0;
        while (index < source.length()) {
            char next = source.charAt(index);
            // 0～31及127(共33个)是控制字符或通信专用字符(其余为可显示字符)
            if (next <= 32) {
                compressBlankChars(index);
            }
            index ++;
        }
        return source.toString();
    }

    private void compressBlankChars(int start) {
        int first = start;
        int move = start + 1;
        if (' ' != (source.charAt(first)) && '\n' != (source.charAt(first))) {
            source.setCharAt(first, ' ');
        }
        while ((move < source.length()) && isBlankChar(source.charAt(move))) {
            if ('\n' == source.charAt(move)) {
                source.setCharAt(first, '\n');
            }
            source.deleteCharAt(move);
        }
    }

    public boolean isBlankChar(char c) {
        if (c <= 32) {
            return true;
        } else {
            return false;
        }
    }
}
