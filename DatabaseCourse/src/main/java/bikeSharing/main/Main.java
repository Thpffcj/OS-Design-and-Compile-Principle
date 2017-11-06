package bikeSharing.main;

import bikeSharing.dao.BikeSharingDao;
import bikeSharing.dao.impl.BikeSharingDaoImpl;

/**
 * Created by Thpffcj on 2017/11/4.
 */
public class Main {

    public static void main(String[] args) {
        BikeSharingDao bikeSharingDao = new BikeSharingDaoImpl();
//        bikeSharingDao.createTable();
//        bikeSharingDao.insertRecordData();
        bikeSharingDao.insertOthersData();
    }
}
