package io;

import java.io.*;

/**
 * Created by Thpffcj on 2017/10/19.
 */
public class FileHandler {

    private static FileHandler fileHandler;
    private static String CONFIGURE_FILE_NAME = "source_path.config";
    private static String TOKEN_FILE_NAME = "token.txt";
    public static String VARIABLE_TABLE_FILE_NAME = "var_table.txt";
    public static String CONSTANT_TABLE_FILE_NAME = "cons_table.txt";
    private static String ERROR_LOG = "Error at line ";

    private File sourceFile;

    private FileHandler() {}

    public static FileHandler getInstance() {
        if (null == fileHandler) {
            fileHandler = new FileHandler();
        }
        return fileHandler;
    }

    public StringBuilder getSource() {

        File file = new File(CONFIGURE_FILE_NAME);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String path = bufferedReader.readLine();
            bufferedReader.close();
            if (null != path) {
                sourceFile = new File(path);
                BufferedReader sourceReader = new BufferedReader(new FileReader(sourceFile));
                StringBuilder sourceCode = new StringBuilder();
                String temp;
                while ((temp = sourceReader.readLine()) != null) {
                    temp = temp + "\n";
                    sourceCode.append(temp);
                }
                sourceReader.close();
                return sourceCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
