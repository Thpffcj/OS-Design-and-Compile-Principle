package cn.edu.nju;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by thpffcj on 2019/11/9.
 */
public class WorkFlowNet {

    public static void getLogOfModel(String modelFile, String logFile) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(modelFile);

            NodeList placeList = document.getElementsByTagName("place");
            System.out.println(placeList.getLength());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String modelFile = "src/main/resources/Model1.pnml";
        String logFile = "src/main/resources/output";

        getLogOfModel(modelFile, logFile);
    }
}
