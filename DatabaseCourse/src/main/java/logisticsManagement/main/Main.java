package logisticsManagement.main;

import logisticsManagement.dao.LogisticsDao;
import logisticsManagement.dao.impl.LogisticsDaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Thpffcj on 2017/11/1.
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        LogisticsDao logisticsDao = new LogisticsDaoImpl();
//        logisticsDao.createTable();
//        logisticsDao.insertData();
//        Set set = logisticsDao.getDormitory("王小星");
//        System.out.println(set);
//        logisticsDao.updateCharge("陶园1舍", 1200);
        logisticsDao.swapDormitory();
    }
}
