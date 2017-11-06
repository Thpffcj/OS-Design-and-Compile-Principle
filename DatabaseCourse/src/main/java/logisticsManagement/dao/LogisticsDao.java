package logisticsManagement.dao;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Thpffcj on 2017/10/31.
 */
public interface LogisticsDao {

    public void createTable();

    public void insertData();

    public Set<String> getDormitory(String name);

    public void updateCharge(String dormitoryName, double charge);

    public void swapDormitory();

}
