package logisticsManagement.dao;

import java.util.List;

/**
 * Created by Thpffcj on 2017/10/31.
 */
public interface LogisticsDao {

    public void createTable();

    public void insertData();

    public List<String> getDormitory(String name);

    public void updateCharge(String dormitoryName, double charge);

    public void swapDormitory();

}
