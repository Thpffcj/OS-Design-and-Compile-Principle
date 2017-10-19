package analyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thpffcj on 2017/10/19.
 */
public class Table {

    public static String CONSTANT_TABLE = "CONSTRANT";
    public static String VARIABLE_TABLE = "VARIABLE";
    private List<Object> items;
    private String tableType;

    public Table(String tableType){
        this.tableType = tableType;
        items = new ArrayList<>();
    }

    /**
     * 将一个常量(变量)装入表，并返回id
     * @param object
     * @return id
     */
    public int add(Object object) {
        if (!items.contains(object)) {
            items.add(object);
            return (items.size() - 1);
        } else {
            return items.indexOf(object);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Object object : items) {
            stringBuilder.append("[" + tableType + "," + i + "," + object + "]");
            stringBuilder.append("\r\n");
            i ++;
        }
        return stringBuilder.toString();
    }
}
