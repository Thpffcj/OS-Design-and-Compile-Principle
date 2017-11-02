package logisticsManagement.dao.impl;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import logisticsManagement.dao.LogisticsDao;

import util.JDBCUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created by Thpffcj on 2017/10/31.
 */
public class LogisticsDaoImpl implements LogisticsDao {

    /**
     * 给出建表代码
     */
    public void createTable() {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String studentSql = "CREATE TABLE `student` (\n" +
                "  `sid` VARCHAR(30) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(50) NOT NULL,\n" +
                "  `faculty` VARCHAR(50) NOT NULL,\n" +
                "  `dormitory_name` VARCHAR(50) NOT NULL,\n" +
                "  `gender` VARCHAR(20) NOT NULL,\n" +
                "  PRIMARY KEY (`sid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        String dormitorySql = "CREATE TABLE `dormitory` (\n" +
                "  `dormitory_name` VARCHAR(50) NOT NULL,\n" +
                "  `campus` VARCHAR(50) NOT NULL,\n" +
                "  `charge` DOUBLE NOT NULL,\n" +
                "  `phone` VARCHAR(50) NOT NULL,\n" +
                "  PRIMARY KEY (`dormitory_name`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        try {
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(studentSql);
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement(dormitorySql);
            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet,preparedStatement,connection);
        }
    }

    /**
     * 给出插入所有数据的代码，并记录所用的时间
     */
    public void insertData() {

        Sheet sheet;
        Workbook book;
        Cell faculty, sid, name, gender, campus, dormitoryName, charge;

        Map<String, String> infos = new HashMap<String, String>();
        Set<String> dormitorys = new HashSet<String>();
        infos = getPhoneByDormitory();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String insertStudent = "insert into student(sid, name, faculty, dormitory_name, gender) values(?,?,?,?,?)";
        String insertDormitory = "insert into dormitory(dormitory_name, campus, charge, phone) values(?,?,?,?)";

        try {
            //t.xls为要读取的excel文件名
            book= Workbook.getWorkbook(new File("E:/GitHub/OS-Design-and-Compile-Principle/DatabaseCourse/src/main/resources/分配方案.xls"));

            //获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
            sheet = book.getSheet(0);

            connection = JDBCUtil.getConnection();

            int rows = sheet.getRows();
            for (int row = 1; row < rows; row ++) {
                //获取每一行的单元格
                faculty = sheet.getCell(0,row);
                sid = sheet.getCell(1,row);
                name = sheet.getCell(2,row);
                gender = sheet.getCell(3,row);
                campus = sheet.getCell(4,row);
                dormitoryName = sheet.getCell(5,row);
                charge = sheet.getCell(6,row);

                preparedStatement = connection.prepareStatement(insertStudent);
                preparedStatement.setString(1, sid.getContents());
                preparedStatement.setString(2, name.getContents());
                preparedStatement.setString(3, faculty.getContents());
                preparedStatement.setString(4, dormitoryName.getContents());
                preparedStatement.setString(5, gender.getContents());
                preparedStatement.execute();


//                if (!dormitorys.contains(dormitoryName.getContents())) {
//                    dormitorys.add(dormitoryName.getContents());
//                    preparedStatement = connection.prepareStatement(insertDormitory);
//                    preparedStatement.setString(1, dormitoryName.getContents());
//                    preparedStatement.setString(2, campus.getContents());
//                    preparedStatement.setDouble(3, Double.valueOf(charge.getContents()));
//                    System.out.println(dormitoryName.getContents() + " " + infos.get(dormitoryName.getContents()));
//                    preparedStatement.setString(4, infos.get(dormitoryName.getContents()));
//                    preparedStatement.execute();
//                }
            }
            book.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet,preparedStatement,connection);
        }


    }

    public Map<String, String> getPhoneByDormitory() {

        File file = new File("src/main/resources/电话.txt");
        Map<String, String> infos = new HashMap<String, String>();
        String info;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((info = bufferedReader.readLine()) != null) {
                String[] temp;
                temp = info.split(";");
                infos.put(temp[0].trim(), temp[1]);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infos;
    }

    /**
     * 查询“王小星”同学所在宿舍楼的所有院系
     * @param name
     * @return
     */
    public List<String> getDormitory(String name) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "SELECT faculty FROM `student` s\n" +
                "WHERE s.dormitory_name IN (\n" +
                "  SELECT s1.dormitory_name FROM `dormitory` s1\n" +
                "  WHERE s1.name = '王小星');";

        List<String> facultys = new ArrayList<String>();

        try {
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            facultys = (List<String>) preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet,preparedStatement,connection);
        }
        return facultys;
    }

    /**
     * 因宿舍装修，陶园一舍的住宿费用提高至 1200 元，在已建数据库的基础上，给出数据库的修改代码
     * @param dormitoryName
     * @param charge
     */
    public void updateCharge(String dormitoryName, double charge) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "UPDATE `dormitory` SET charge = 1200 WHERE dormitory_name = '陶园一舍';";

        try {
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement, connection);
        }
    }


    /**
     * 在 5 的基础上，假设软件学院男女研究生互换宿舍楼，给出数据库的修改代码，并记录所用的时间
     */
    public void swapDormitory() {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql1 = "SELECT dormitory_name FROM `student`\n" +
                "WHERE faculty = '软件学院' AND gender = '男';";

        try {
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql1);
            resultSet = preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement, connection);
        }

    }


}
