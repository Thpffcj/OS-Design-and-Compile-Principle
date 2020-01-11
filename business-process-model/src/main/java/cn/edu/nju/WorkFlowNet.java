package cn.edu.nju;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by thpffcj on 2019/11/9.
 */
public class WorkFlowNet {

    Map<String, Place> placeMap = new HashMap<>();
    Map<String, Transition> transitionMap = new HashMap<>();

    int placeNumber = 0;
    // 每个token位置对应的place
    Map<Integer, Place> tokenPlace = new HashMap<>();
    // 每个place对应的的token位置
    Map<String, Integer> placeToken = new HashMap<>();

    // 开始状态
    List<Integer> startPosition = new ArrayList<>();
    // 结束状态
    List<Integer> endPosition = new ArrayList<>();

    private static class Place {

        String id;
        boolean isVisited = false;
        // 入度，出度
        int in = 0;
        int out = 0;
        List<Transition> lastTransitions;
        List<Transition> nextTransitions;

        public Place(String id) {
            this.id = id;
            lastTransitions = new ArrayList<>();
            nextTransitions = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Place{" +
                    "id='" + id + '\'' +
                    ", isVisited=" + isVisited +
                    ", in=" + in +
                    ", out=" + out +
                    ", lastTransitions=" + lastTransitions +
                    ", nextTransitions=" + nextTransitions +
                    '}';
        }
    }

    private static class Transition {

        String id;
        String name;
        // 入度，出度
        int in = 0;
        int out = 0;
        boolean isVisited = false;
        List<Place> lastPlaces;
        List<Place> nextPlaces;
        List<Integer> nextState;

        public Transition(String id, String name) {
            this.id = id;
            this.name = name;
            lastPlaces = new ArrayList<>();
            nextPlaces = new ArrayList<>();
            nextState = new ArrayList<>();
        }
    }

    public void getLogOfModel(String modelFile, String logFile) {

        placeMap = new HashMap<>();
        transitionMap = new HashMap<>();
        tokenPlace = new HashMap<>();
        placeToken = new HashMap<>();

        // 解析pnml源文件
        parsePnml(modelFile);

        startPosition = new ArrayList<>(Collections.nCopies(placeNumber, 0));
        endPosition = new ArrayList<>(Collections.nCopies(placeNumber, 0));

        // 获得起始状态和终止状态的token状态
        findStart();
        findEnd();
        System.out.println("开始状态：" + startPosition);
        System.out.println("结束状态：" + endPosition);

        // 获得过程日志
        List<String> result = new ArrayList<>();
        getSequence(startPosition, "", result);

        Set<String> set = new HashSet<>();
        set.addAll(result);
        System.out.println("过程日志数量：" + set.size());
        for (String s : set) {
            System.out.println(s);
        }

        try {
            FileWriter fileWriter = new FileWriter(logFile);
            for (String s : set) {
                fileWriter.write(s + "\r\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析pnml文件
     *
     * @param modelFile 输入文件
     */
    private void parsePnml(String modelFile) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(modelFile);

            NodeList placeList = document.getElementsByTagName("place");
            placeNumber = placeList.getLength();
            int position = 0;
            for (int i = 0; i < placeList.getLength(); i++) {
                // 获取place id
                String placeId = placeList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                Place place = new Place(placeId);
                placeMap.put(placeId, place);
                // 每个token位置对应的place
                tokenPlace.put(position, place);
                placeToken.put(place.id, position);
                position++;
//                System.out.println(place);
            }

            NodeList transitionList = document.getElementsByTagName("transition");
            for (int i = 0; i < transitionList.getLength(); i++) {
                // 获取transition id
                String transitionId = transitionList.item(i).getAttributes().getNamedItem("id").getNodeValue();

                // 获取子节点
                NodeList childNodes = transitionList.item(i).getChildNodes();
                String transitionName = "";
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node childNode = childNodes.item(j);
                    String nodeName = childNode.getNodeName();
                    // 当子节点为name时
                    if ("name".equals(nodeName)) {
                        // TODO 为什么name有三个节点
                        transitionName = childNode.getTextContent().trim();
                    }
                }

                Transition transition = new Transition(transitionId, transitionName);
                transitionMap.put(transitionId, transition);
//                System.out.println(transition);
            }

            NodeList arcList = document.getElementsByTagName("arc");
            for (int i = 0; i < arcList.getLength(); i++) {
                String arcId = arcList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                String source = arcList.item(i).getAttributes().getNamedItem("source").getNodeValue();
                String target = arcList.item(i).getAttributes().getNamedItem("target").getNodeValue();
//                System.out.println(arcId + " " + source + " " + target);

                // 添加边关系
                String type = source.split("_")[0];
                if (type.equals("place")) {
                    Place place = placeMap.get(source);
                    Transition transition = transitionMap.get(target);
                    // Place出度+1
                    place.out++;
                    place.nextTransitions.add(transition);
                    transition.lastPlaces.add(place);

                    // Transition入度+1
                    transitionMap.get(target).in++;
                } else {
                    Place place = placeMap.get(target);
                    Transition transition = transitionMap.get(source);
                    // Transition出度+1
                    transition.out++;
                    transition.nextPlaces.add(place);
                    place.lastTransitions.add(transition);

                    // Place入度+1
                    placeMap.get(target).in++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 找到起始库所
     *
     * @return
     */
    public void findStart() {
        List<Map.Entry<Integer, Place>> list = new ArrayList<>(tokenPlace.entrySet());
        for (Map.Entry<Integer, Place> entry : list) {
            if (entry.getValue().in == 0) {
                int position = entry.getKey();
                startPosition.set(position, 1);
            }
        }
    }

    /**
     * 找到终止库所
     *
     * @return
     */
    public void findEnd() {
        List<Map.Entry<Integer, Place>> list = new ArrayList<>(tokenPlace.entrySet());
        for (Map.Entry<Integer, Place> entry : list) {
            if (entry.getValue().out == 0) {
                // 终止库所所在的token位置
                int position = entry.getKey();
                endPosition.set(position, 1);
            }
        }
    }

    /**
     * 找到起始库开始的所有序列
     *
     * @param token        状态
     * @param path         当前序列
     * @param sequenceList 序列集合
     */
    public void getSequence(
            List<Integer> token, String path, List<String> sequenceList) {

        if (endPosition.equals(token)) {
            sequenceList.add(path.substring(0, path.length() - 1));
            return;
        }

        for (int position = 0; position < token.size(); position++) {
            // 找到对应的库所
            if (token.get(position) == 1) {
                Place place = tokenPlace.get(position);

                // 找到该库所的下一个变迁
                List<Transition> transitions = place.nextTransitions;
                for (int i = 0; i < transitions.size(); i++) {
                    Transition transition = transitions.get(i);

                    // 该变迁没有访问过
                    if (transitions.size() == 1 || !transition.isVisited) {
                        List<Place> lastPlaces = transition.lastPlaces;
                        List<Place> nextPlaces = transition.nextPlaces;
                        List<Integer> curToken = new ArrayList<>();
                        curToken.addAll(token);

                        // 该变迁能否执行
                        boolean flag = true;
                        for (Place lastPlace : lastPlaces) {
                            if (curToken.get(placeToken.get(lastPlace.id)) == 0) {
                                flag = false;
                            }
                        }

                        // 状态允许转换
                        if (flag) {
                            transition.isVisited = true;
                            String s = path;
                            s += transition.name + " ";

                            // 状态转换
                            for (Place lastPlace : lastPlaces) {
                                int lastPosition = placeToken.get(lastPlace.id);
                                curToken.set(lastPosition, 0);
                            }
                            for (Place nextPlace : nextPlaces) {
                                int nextPosition = placeToken.get(nextPlace.id);
                                curToken.set(nextPosition, 1);
                            }

                            getSequence(curToken, s, sequenceList);

                            transition.isVisited = false;
                            curToken = token;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        WorkFlowNet workFlowNet = new WorkFlowNet();

        String modelFile1 = "src/main/resources/model/model1.pnml";
        String logFile1 = "src/main/resources/output/1.log";

        String modelFile2 = "src/main/resources/model/model2.pnml";
        String logFile2 = "src/main/resources/output/2.log";

        String modelFile3 = "src/main/resources/model/model3.pnml";
        String logFile3 = "src/main/resources/output/3.log";

        String modelFile4 = "src/main/resources/model/model4.pnml";
        String logFile4 = "src/main/resources/output/4.log";

        String modelFile5 = "src/main/resources/model/model5.pnml";
        String logFile5 = "src/main/resources/output/5.log";

        String modelFile6 = "src/main/resources/model/model6.pnml";
        String logFile6 = "src/main/resources/output/6.log";

        String modelFile7 = "src/main/resources/model/model7.pnml";
        String logFile7 = "src/main/resources/output/7.log";

        workFlowNet.getLogOfModel(modelFile1, logFile1);
        workFlowNet.getLogOfModel(modelFile2, logFile2);
        workFlowNet.getLogOfModel(modelFile3, logFile3);
        workFlowNet.getLogOfModel(modelFile4, logFile4);
        workFlowNet.getLogOfModel(modelFile5, logFile5);
        workFlowNet.getLogOfModel(modelFile6, logFile6);
        workFlowNet.getLogOfModel(modelFile7, logFile7);
    }
}
