package cn.edu.nju;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thpffcj on 2019/11/9.
 */
public class WorkFlowNetTest {

    Map<String, Place> placeMap = new HashMap<>();
    Map<String, Transition> transitionMap = new HashMap<>();

    private static class Place {

        String id;
        boolean isVisited = false;
        // 入度，出度
        int in = 0;
        int out = 0;
        List<Transition> transitions;

        public Place(String id) {
            this.id = id;
            transitions = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Place{" +
                    "id='" + id + '\'' +
                    ", isVisited=" + isVisited +
                    ", transitions=" + transitions +
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
        List<Place> places;

        public Transition(String id, String name) {
            this.id = id;
            this.name = name;
            places = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Transition{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", isVisited=" + isVisited +
                    ", places=" + places +
                    '}';
        }
    }

    public void getLogOfModel(String modelFile, String logFile) {

        placeMap = new HashMap<>();
        transitionMap = new HashMap<>();

        // 解析pnml源文件
        parsePnml(modelFile);

        Place start = findStart();
        Place end = findEnd();

        if (start == null) {
            return;
        }

        // 获得过程日志
        List<String> result = new ArrayList<>();
        getSequence(start, end, "", result);

        System.out.println("过程日志数量：" + result.size());
        for (String s : result) {
            System.out.println(s);
        }
    }

    /**
     * 解析pnml文件
     * @param modelFile 输入文件
     */
    private void parsePnml(String modelFile) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(modelFile);

            NodeList placeList = document.getElementsByTagName("place");
            for (int i = 0; i < placeList.getLength(); i++) {
                // 获取place id
                String placeId = placeList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                Place place = new Place(placeId);
                placeMap.put(placeId, place);
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
                    if ("name".equals(nodeName)){
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
                    // Place出度+1
                    place.out++;
                    place.transitions.add(transitionMap.get(target));

                    System.out.println(place);

                    // Transition入度+1
                    transitionMap.get(target).in++;
                } else {
                    Transition transition = transitionMap.get(source);
                    // Transition出度+1
                    transition.out++;
                    transition.places.add(placeMap.get(target));

                    // Place入度+1
                    placeMap.get(target).in++;
                }
            }
//            System.out.println("---------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 找到起始库所
     * @return 日志起始库所
     */
    public Place findStart() {
        List<Map.Entry<String, Place>> list = new ArrayList<>(placeMap.entrySet());
        for (Map.Entry<String, Place> entry : list) {
            if (entry.getValue().in == 0) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 找到终止库所
     * @return
     */
    public Place findEnd() {
        List<Map.Entry<String, Place>> list = new ArrayList<>(placeMap.entrySet());
        for (Map.Entry<String, Place> entry : list) {
            if (entry.getValue().out == 0) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 找到起始库所到终止库所的所有序列
     * @param startPlace 起始库所
     * @param endPlace 终止库所
     * @param path 当前序列
     * @param sequenceList 序列集合
     */
    public void getSequence(
            Place startPlace, Place endPlace, String path, List<String> sequenceList) {

        if (startPlace == endPlace) {
            sequenceList.add(path);
            return;
        }

        // 库所已经访问过
        if (startPlace.isVisited) {

        } else {
            startPlace.isVisited = true;

            // 找到该库所的下一个变迁
            List<Transition> transitions = startPlace.transitions;
            for (int i = 0; i < transitions.size(); i++) {
                Transition transition = transitions.get(i);

                // 该变迁没有访问过
                if (!transition.isVisited) {
                    transition.isVisited = true;

                    String s = path;
                    s += transition.name + " ";

                    // 变迁后只有一个库所
                    if (transition.places.size() == 1) {
                        Place nextPlace = transition.places.get(0);
                        getSequence(nextPlace, endPlace, s, sequenceList);
                    } else {  // 并行结构
                        List<List<String>> allParallel = new ArrayList<>();

                        // 遍历所有的并行分支，加入allParallel
                        for (Place parallelStartPlace : transition.places) {
                            // 找到并行结构的结束库所
                            Place parallelEndPlace = getParallelEndPlace(parallelStartPlace);
                            System.out.println("结束库所：" + parallelEndPlace);

                            // 找到并行结构的所有序列，放在parallel中
                            List<String> parallel = new ArrayList<>();
                            getSequence(parallelStartPlace, parallelEndPlace, "", parallel);

                            // 每个并行结构的排列放入allParallel
                            allParallel.add(parallel);
                            System.out.println(parallelStartPlace.id + " 的序列个数为：" + parallel.size());
                        }
                        System.out.println(transition.name + " 有 " + allParallel.size() + " 个并行结构");

                        // 找到并行结构的全排列
                        List<String> allParallelPath = new ArrayList<>();
                        for (List<String> parallel : allParallel) {
                            
                        }
                        findAllPermutations(allParallel, allParallelPath);

                    }
                    transition.isVisited = false;
                }
            }
            startPlace.isVisited = false;
        }

        return;
    }

    /**
     * 找到并行结构的结束库所
     * @param startPlace 并行结构的开始库所
     * @return 并行结构的结束库所
     */
    public Place getParallelEndPlace(Place startPlace) {
        List<Transition> transitions = startPlace.transitions;

        // TODO 结束变迁？未考虑并行之中包含并行
        if (transitions.size() == 1 && transitions.get(0).in > 1 && transitions.get(0).out == 1) {
            return startPlace;
        }

        return getParallelEndPlace(transitions.get(0).places.get(0));
    }

    /**
     * 找到并行结构的全排列
     * @param allParallel
     * @param allParallelPath
     */
    public void findAllPermutations(List<List<String>> allParallel, List<String> allParallelPath) {

    }


    public static void main(String[] args) {

        WorkFlowNetTest workFlowNetTest = new WorkFlowNetTest();

        String modelFile1 = "src/main/resources/model/model1.pnml";
        String logFile1 = "src/main/resources/output/1.log";

        workFlowNetTest.getLogOfModel(modelFile1, logFile1);
    }
}
