# 已知关系模式
# 提交记录表 commit(sha,total_add,total_delete,datetime,author)
# 文件更改表 file(sha, filename, add_line, delete_line, datetime)
# 迭代表 deadline(id, start_day, end_day, name)
# 记录提交表中记录了项目代码提交的编号，提交的新增行数，删除行数，日期，提交作者。
# 文件更改表中记录了项目文件提交的编号，文件名，文件增加的行数，文件删除的行数，提交的日期。
# 迭代表中记录了项目每一个迭代的起止时间。

# 1. 删除提交记录表中增加代码行数大于 5000 行，删除代码行数小于 100 行的提交记录
DELETE FROM `commit`
WHERE commit.total_add > 500 AND commit.total_delete < 100;

# 2. 查询项目中每一个迭代每一个学生的代码提交数量，显示迭代 id，学生姓名，代码行数
SELECT deadline.id, commit.author, commit.total_add, commit.total_delete FROM `commit`, `deadline`
WHERE commit.datetime BETWEEN deadline.start_day AND deadline.end_day GROUP BY deadline.id, commit.author;

# 3. 查询项目中所有的 java 文件占总文件数量的比例，显示 java 文件的数量，总文件的数量
SELECT * FROM (
  (SELECT COUNT(DISTINCT filename) FROM `file` WHERE filename LIKE '%.java' ) AS java,
  (SELECT COUNT(DISTINCT filename) FROM `file` ) AS total);

# 4. 查询项目过程中每个迭代中提交代码次数最多的日期，显示迭代号，提交日期，对应日期提交的次数
SELECT  d.id, DATE_FORMAT(c.datetime, '%Y-%m-%d'), COUNT(c.sha) FROM `commit` c, `deadline` d
WHERE ((d.id = 1 AND DATE_FORMAT(c.datetime, '%Y-%m-%d') = (
  SELECT DATE_FORMAT(c1.datetime, '%Y-%m-%d') FROM `commit` c1, `deadline` d1
  WHERE d1.id = 1 AND c1.datetime BETWEEN d1.start_day AND d1.end_day
  GROUP BY d1.id, DATE_FORMAT(c1.datetime, '%Y-%m-%d')
  ORDER BY COUNT(c1.sha) DESC LIMIT 0, 1))
       OR (d.id = 2 AND DATE_FORMAT(c.datetime, '%Y-%m-%d') = (
  SELECT DATE_FORMAT(c2.datetime, '%Y-%m-%d') FROM `commit` c2, `deadline` d2
  WHERE d2.id = 2 AND c2.datetime BETWEEN d2.start_day AND d2.end_day
  GROUP BY d2.id, DATE_FORMAT(c2.datetime, '%Y-%m-%d')
  ORDER BY COUNT(c2.sha) DESC LIMIT 0, 1))
       OR (d.id = 3 AND DATE_FORMAT(c.datetime, '%Y-%m-%d') = (
  SELECT DATE_FORMAT(c3.datetime, '%Y-%m-%d') FROM `commit` c3, `deadline` d3
  WHERE d3.id = 3 AND c3.datetime BETWEEN d3.start_day AND d3.end_day
  GROUP BY d3.id, DATE_FORMAT(c3.datetime, '%Y-%m-%d')
  ORDER BY COUNT(c3.sha) DESC LIMIT 0, 1)))
GROUP BY d.id;

# 5. 查询所有的文件行数超过 200 行的 java 文件（假设每个文件的初始行数为 0行），并按照降序排列，显示文件名，文件的代码行数
SELECT filename, total FROM (
SELECT filename, SUM(add_line) - SUM(delete_line) AS total FROM `file`
WHERE filename LIKE '%.java'
GROUP BY filename ORDER BY total DESC) total_line WHERE total > 200;

# 6. 更新迭代表中迭代三的结束日期为原来结束日期的一周
UPDATE `deadline` SET end_day = DATE_ADD(end_day, INTERVAL 1 WEEK) WHERE name = '迭代三';
