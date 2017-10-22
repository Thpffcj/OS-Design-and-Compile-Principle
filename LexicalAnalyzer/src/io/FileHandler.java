package io;

import analyzer.Table;
import analyzer.Token;

import java.io.*;
import java.util.List;

/**
 * Created by Thpffcj on 2017/10/19.
 */
public class FileHandler {

    private static FileHandler fileHandler;
    private static String CONFIGURE_FILE_NAME = "src/source_path.config";
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
//                System.out.println("sourceCode " + sourceCode);
                sourceReader.close();
                return sourceCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void outputToken(List<Token> tokens) {
//        String path = getSourceParentPath();
        String path = TOKEN_FILE_NAME;
        File tokenFile = openOutputFile(path);
        StringBuilder tokenContent = new StringBuilder();
        for (Token token : tokens) {
            tokenContent.append(token);
            tokenContent.append("\r\n");
        }
        writeFile(tokenFile, tokenContent.toString());
    }

    public void outputTable(Table table, String fileName) {
//        String path = getSourceParentPath();
        String path = fileName;
        File varTableFile = openOutputFile(path);
        writeFile(varTableFile, table.toString());
    }

    public void outputError(int lineNum) {
//        String path = getSourceParentPath();
        String path =  TOKEN_FILE_NAME;
        File tokenFile = openOutputFile(path);
        String errorInfo = ERROR_LOG + lineNum;
        writeFile(tokenFile, errorInfo);
    }

    private String getSourceParentPath(){
        String path = sourceFile.getParent();
        if (path == null) {
            path = "";
        }
        return path;
    }

    private File openOutputFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private void writeFile(File file, String content) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
