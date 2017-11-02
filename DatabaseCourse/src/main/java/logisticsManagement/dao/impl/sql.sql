CREATE TABLE `student` (
  `sid` int NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `faculty` VARCHAR(20) NOT NULL,
  `dormitory_name` VARCHAR(20) NOT NULL,
  `gender` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `dormitory` (
  `dormitory_name` VARCHAR(20) NOT NULL,
  `campus` VARCHAR(20) NOT NULL,
  `charge` DOUBLE NOT NULL,
  `phone` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`dormitory_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SELECT faculty FROM `student` s
WHERE s.dormitory_name IN (
  SELECT s1.dormitory_name FROM `dormitory` s1
  WHERE s1.name = '王小星');

UPDATE `dormitory` SET charge = 1200 WHERE dormitory_name = '陶园一舍';

SELECT dormitory_name FROM `student`
WHERE faculty = '软件学院' AND gender = '男';

SELECT dormitory_name FROM `student`
WHERE faculty = '软件学院' AND gender = '女';

UPDATE `student` SET dormitory_name = ''
WHERE faculty = '软件学院' AND gender = '男';

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

System.out.println(name);
//                System.out.println(String.valueOf(charge));

//                if (!dormitorys.contains(String.valueOf(dormitoryName))) {
//                    preparedStatement = connection.prepareStatement(insertDormitory);
//                    preparedStatement.setString(1, String.valueOf(dormitoryName));
//                    preparedStatement.setString(2, String.valueOf(campus));
//                    preparedStatement.setDouble(3, Double.valueOf(String.valueOf(charge)));
//                    preparedStatement.setString(4, infos.get(String.valueOf(dormitoryName)));
//                    preparedStatement.execute();
//                    dormitorys.add(String.valueOf(dormitoryName));
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