package bikeSharing.dao.impl;

import bikeSharing.dao.BikeSharingDao;
import util.JDBCUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thpffcj on 2017/11/3.
 */
public class BikeSharingDaoImpl implements BikeSharingDao {

    /**
     * 给出建表代码
     */
    public void createTable() {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String userSql = "CREATE TABLE `user` (\n" +
                "  `uid` int NOT NULL,\n" +
                "  `name` VARCHAR(50) NOT NULL,\n" +
                "  `phone` VARCHAR(50) NOT NULL,\n" +
                "  `address` VARCHAR(100) NULL ,\n" +
                "  `charge` DOUBLE NOT NULL,\n" +
                "  PRIMARY KEY (`uid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        String bikeSql = "CREATE TABLE `bike` (\n" +
                "  `bid` int NOT NULL,\n" +
                "  `time` DOUBLE NOT NULL,\n" +
                "  PRIMARY KEY (`bid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";

        String maintainSql = "CREATE TABLE `maintain` (\n" +
                "  `bid` int NOT NULL,\n" +
                "  `end` VARCHAR(50) NOT NULL,\n" +
                "  PRIMARY KEY (`bid`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        String rideSql = "CREATE TABLE `ride` (\n" +
                "  `uid` int NOT NULL,\n" +
                "  `bid` int NOT NULL,\n" +
                "  `begin` VARCHAR(50) NOT NULL,\n" +
                "  `begin_time` DATETIME NOT NULL,\n" +
                "  `end` VARCHAR(50) NOT NULL,\n" +
                "  `end_time` DATETIME NOT NULL,\n" +
                "  `money` DOUBLE NOT NULL\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        try {
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(userSql);
            preparedStatement.execute();
//            preparedStatement = connection.prepareStatement(bikeSql);
//            preparedStatement.execute();
//            preparedStatement = connection.prepareStatement(maintainSql);
//            preparedStatement.execute();
//            preparedStatement = connection.prepareStatement(rideSql);
//            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet,preparedStatement,connection);
        }
    }

    /**
     * 给出插入record数据的代码，并记录所用的时间
     */
    public void insertRecordData() {

        long startTime = System.currentTimeMillis();

        File recordFile = new File("src/main/resources/record.txt");

        String info;
        int userId;
        int bikeId;
        String begin;
        String beginTime;
        String end;
        String engTime;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String insertRide = "insert into ride(uid, bid, begin, begin_time, end, end_time) values(?,?,?,?,?,?)";

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(recordFile));

            connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(insertRide);

            int times = 0;
            while ((info = bufferedReader.readLine()) != null) {
                String[] temp;
                temp = info.split(";");
                userId = Integer.parseInt(temp[0]);
                bikeId = Integer.parseInt(temp[1]);
                begin = temp[2];
                beginTime = temp[3];
                end = temp[4];
                engTime = temp[5];

                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, bikeId);
                preparedStatement.setString(3, begin);
                preparedStatement.setString(4, beginTime);
                preparedStatement.setString(5, end);
                preparedStatement.setString(6, engTime);
                preparedStatement.addBatch();

                if (times >= 50000) {
                    preparedStatement.executeBatch();
                    connection.commit();
                    times = 0;
                }
                times++;
            }

            preparedStatement.executeBatch();
            connection.commit();
            bufferedReader.close();
        }  catch(Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement, connection);
            long endTime = System.currentTimeMillis();
            System.out.println("插入所有数据共用时间： " + (endTime - startTime) / 1000.0 + "s");
        }
    }

    /**
     * 给出插入除record数据的代码，并记录所用的时间
     */
    public void insertOthersData() {

        long startTime = System.currentTimeMillis();

        File userFile = new File("src/main/resources/user.txt");
        File bikeFile = new File("src/main/resources/bike.txt");

        String info;
        int userId;
        String name;
        String phone;
        double charge;

        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet = null;
        String insertUser = "insert into user(uid, name, phone, charge) values(?,?,?,?)";
        String insertBike = "insert into bike(bid, time) values(?, ?)";

        try {
            // 插入user表
            BufferedReader bufferedReader = new BufferedReader(new FileReader(userFile));

            connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            preparedStatement1 = connection.prepareStatement(insertUser);

            int times = 0;
            while ((info = bufferedReader.readLine()) != null) {
                String[] temp;
                temp = info.split(";");
                userId = Integer.parseInt(temp[0]);
                name = temp[1];
                phone = temp[2];
                charge = Double.parseDouble(temp[3]);

                preparedStatement1.setInt(1, userId);
                preparedStatement1.setString(2, name);
                preparedStatement1.setString(3, phone);
                preparedStatement1.setDouble(4, charge);
                preparedStatement1.addBatch();

                if (times >= 50000) {
                    preparedStatement1.executeBatch();
                    connection.commit();
                    times = 0;
                }
                times++;
            }

            preparedStatement1.executeBatch();
            connection.commit();

            // 插入bike表
            bufferedReader = new BufferedReader(new FileReader(bikeFile));

            preparedStatement2 = connection.prepareStatement(insertBike);

            while ((info = bufferedReader.readLine()) != null) {
                preparedStatement2.setInt(1, Integer.parseInt(info));
                preparedStatement2.setDouble(2, 0.0);
                preparedStatement2.addBatch();
            }

            preparedStatement2.executeBatch();
            connection.commit();

            bufferedReader.close();
        }  catch(Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement1, connection);
            JDBCUtil.release(resultSet, preparedStatement2, connection);
            long endTime = System.currentTimeMillis();
            System.out.println("插入所有数据共用时间： " + (endTime - startTime) / 1000.0 + "s");
        }
    }

    /**
     * 查询出所有用户在晚上6点到12点之间，单车归还的最频繁的地点，作为家庭住址添加在用户表中
     */
    public void insertAddress() {

        long startTime = System.currentTimeMillis();

        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet = null;

        String selectAddress = "SELECT A.uid, A.end, Max(A.time), name, phone, charge FROM\n" +
                "  (SELECT r.uid, r.end, COUNT(r.end) AS time FROM ride r\n" +
                "  WHERE DATE_FORMAT(r.end_time , \"%H\") BETWEEN '18' AND '24'\n" +
                "  GROUP BY r.uid,r.end\n" +
                "   ORDER BY r.uid, time DESC) AS A, `user` WHERE A.uid = user.uid\n" +
                "GROUP BY A.uid;";
        String insertAddress = "REPLACE INTO `user`(uid, name, phone, address, charge) VALUES (?, ?, ?, ?, ?);";

        try {

            connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            preparedStatement1 = connection.prepareStatement(selectAddress);
            preparedStatement2 = connection.prepareStatement(insertAddress);

            resultSet = preparedStatement1.executeQuery();

            int userId = -1;
            String username = null;
            String phone = null;
            String address = null;
            double charge = 0.0;

            int times = 0;
            while (resultSet.next()) {

                userId = resultSet.getInt(1);
                address = resultSet.getString(2);
                username = resultSet.getString(4);
                phone = resultSet.getString(5);
                charge = resultSet.getDouble(6);

                preparedStatement2.setInt(1, userId);
                preparedStatement2.setString(2, username);
                preparedStatement2.setString(3, phone);
                preparedStatement2.setString(4, address);
                preparedStatement2.setDouble(5, charge);
                preparedStatement2.addBatch();

                if (times >= 50000) {
                    preparedStatement2.executeBatch();
                    connection.commit();
                    times = 0;
                }
                times++;
            }

            preparedStatement2.executeBatch();
            connection.commit();

        }  catch(Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement1, connection);
            JDBCUtil.release(resultSet, preparedStatement2, connection);
            long endTime = System.currentTimeMillis();
            System.out.println("插入所有用户地址： " + (endTime - startTime) / 1000.0 + "s");
        }
    }

    /**
     * 根据单车使用记录中的使用时间，自动补全费用字段，并在用户账户中，扣除相应的金额。
     */
    public void insertMoney() {

        long startTime = System.currentTimeMillis();

        Map<Integer, Double> bikeTime = new HashMap<Integer, Double>();

        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement preparedStatement4 = null;
        PreparedStatement preparedStatement5 = null;
        ResultSet resultSet = null;
        ResultSet resultSetBike = null;

        String sql = "SELECT user.uid, name, phone, charge, bid, begin, begin_time, end, end_time, EXTRACT(HOUR_MINUTE FROM end_time) - EXTRACT(HOUR_MINUTE FROM begin_time), address FROM `ride`, `user` WHERE ride.uid = user.uid;";
        String sql1 = "SELECT bid, time FROM `bike`;";
        String updateUserSql = "REPLACE INTO `user`(uid, name, phone, address, charge) VALUES (?, ?, ?, ?, ?);";
        String updateBikeSql = "REPLACE INTO `bike`(bid, time) VALUES (?, ?);";
        String updateRideSql = "REPLACE INTO `ride`(uid, bid, begin, begin_time, end, end_time, money) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try {
            connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement5 = connection.prepareStatement(sql1);
            preparedStatement2 = connection.prepareStatement(updateUserSql);
            preparedStatement3 = connection.prepareStatement(updateBikeSql);
            preparedStatement4 = connection.prepareStatement(updateRideSql);

            resultSetBike = preparedStatement5.executeQuery();
            while (resultSetBike.next()) {
                bikeTime.put(resultSetBike.getInt(1), resultSetBike.getDouble(2));
            }

            resultSet = preparedStatement1.executeQuery();

            int uid = -1;
            String username = null;
            String phone = null;
            double charge = 0.0;
            int bid = -1;
            String begin = null;
            String beginTime = null;
            String end = null;
            String endTime = null;
            int miniutes = -1;
            String address;
            double time = 0.0;

            double money = 0.0;
            int times = 0;
            while (resultSet.next()) {
                uid = resultSet.getInt(1);
                username = resultSet.getString(2);
                phone = resultSet.getString(3);
                charge = resultSet.getDouble(4);
                bid = resultSet.getInt(5);
                begin = resultSet.getString(6);
                beginTime = resultSet.getString(7);
                end = resultSet.getString(8);
                endTime = resultSet.getString(9);
                miniutes = resultSet.getInt(10);
                address = resultSet.getString(11);
                time = bikeTime.get(bid);
                money = getMoney(miniutes);
                time = time + miniutes;
                charge = charge - money;

                preparedStatement2.setInt(1, uid);
                preparedStatement2.setString(2, username);
                preparedStatement2.setString(3, address);
                preparedStatement2.setString(4, phone);
                preparedStatement2.setDouble(5, charge);
                preparedStatement2.addBatch();

                preparedStatement3.setInt(1, bid);
                preparedStatement3.setInt(2, (int) time);
                preparedStatement3.addBatch();

                preparedStatement4.setInt(1, uid);
                preparedStatement4.setInt(2, bid);
                preparedStatement4.setString(3, begin);
                preparedStatement4.setString(4, beginTime);
                preparedStatement4.setString(5, end);
                preparedStatement4.setString(6, endTime);
                preparedStatement4.setDouble(7, money);
                preparedStatement4.addBatch();

                if (times >= 50000) {
                    preparedStatement2.executeBatch();
                    preparedStatement3.executeBatch();
                    preparedStatement4.executeBatch();
                    connection.commit();
                    preparedStatement2.clearBatch();
                    preparedStatement3.clearBatch();
                    preparedStatement4.clearBatch();
                    times = 0;
                }
                System.out.println(times);
                times++;
            }

            preparedStatement2.executeBatch();
            preparedStatement3.executeBatch();
            preparedStatement4.executeBatch();
            connection.commit();
            preparedStatement2.clearBatch();
            preparedStatement3.clearBatch();
            preparedStatement4.clearBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement1, connection);
            JDBCUtil.release(resultSet, preparedStatement2, connection);
            JDBCUtil.release(resultSet, preparedStatement3, connection);
            JDBCUtil.release(resultSet, preparedStatement4, connection);
            long endTime = System.currentTimeMillis();
            System.out.println("自动补全费用字段，并在用户账户中，扣除相应的金额： " + (endTime - startTime) / 1000.0 + "s");
        }
    }

    private double getMoney(int minutes) {
        if (minutes < 30) {
            return 1.0;
        } else if (minutes >= 30 && minutes < 60) {
            return 2.0;
        } else if (minutes >= 60 && minutes < 90) {
            return 3.0;
        } else {
            return 4.0;
        }
    }

    /**
     * 维护一个单车维修表，包括单车编号和单车最后使用地点
     */
    public void findMaintain() {

        long startTime = System.currentTimeMillis();

        List<Integer> bikeNeedMaintain = new ArrayList<Integer>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet = null;

        String bikeSql = "SELECT bid FROM bike WHERE time > 12000;";
        String endSql = "SELECT bid, end FROM ride WHERE bid = ? ORDER BY end_time Desc LIMIT 0,1;";
        String updateSql = "REPLACE INTO `maintain`(bid, end) VALUES (?, ?);";

        try {
            connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(bikeSql);
            preparedStatement2 = connection.prepareStatement(updateSql);

            resultSet = preparedStatement.executeQuery();

            int bid = -1;
            String end = null;
            int times = 0;
            while (resultSet.next()) {
                bid = resultSet.getInt(1);
                bikeNeedMaintain.add(bid);
            }

            preparedStatement = connection.prepareStatement(endSql);
            for(int b : bikeNeedMaintain) {
                preparedStatement.setInt(1, b);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    bid = resultSet.getInt(1);
                    end = resultSet.getString(2);

                    preparedStatement2.setInt(1, bid);
                    preparedStatement2.setString(2, end);
                    preparedStatement2.addBatch();
                }
            }

            preparedStatement2.executeBatch();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement, connection);
            long endTime = System.currentTimeMillis();
            System.out.println("维护一个单车维修表： " + (endTime - startTime) / 1000.0 + "s");
        }
    }

}
