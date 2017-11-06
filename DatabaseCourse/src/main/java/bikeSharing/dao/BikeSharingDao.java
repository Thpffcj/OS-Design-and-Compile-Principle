package bikeSharing.dao;

/**
 * Created by Thpffcj on 2017/11/3.
 */
public interface BikeSharingDao {

    public void createTable();

    public void insertRecordData();

    public void insertOthersData();

    public void insertAddress();

    public void insertMoney();

    public void findMaintain();
}
