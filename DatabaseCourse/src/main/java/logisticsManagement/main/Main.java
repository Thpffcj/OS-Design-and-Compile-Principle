package logisticsManagement.main;

import logisticsManagement.dao.LogisticsDao;
import logisticsManagement.dao.impl.LogisticsDaoImpl;

/**
 * Created by Thpffcj on 2017/11/1.
 */
public class Main {

    public static void main(String[] args) {
        LogisticsDao logisticsDao = new LogisticsDaoImpl();
//        logisticsDao.createTable();
        logisticsDao.insertData();
    }
}
