# 某付费文章阅读平台的部分关系模式如下
# 文章作者表 platform_writer(writer_id, writer_name, writer_email, create_time)
# 文章表 platform_article(article_id, writer_id, article_title, content, create_time)
# 读者表 platform_reader(reader_id, reader_name, create_time)
# 付费账单表 platform_deal(deal_id, article_id, reader_id, deal_payment, create_time)

# 其中，create_time 为 datetime 类型，deal_payment 为单笔交易的付费金额，double
# 类型，金额多少由读者根据自己的意愿自行输入，每篇文章每个读者只需且只能
# 付费一次。

# (1) 为数据库来自 IP120.55.91.83 的用户 writer, 密码为 writer， 设置文章作者表的增改查权限(该数据库的 schema
# 名称为 platform)
GRANT INSERT, UPDATE, SELECT
ON platform.platform_writer
to writer@120.55.190.126 identified by 'writer';

# (2) 查询姓名为 zoe 的读者最近付费的 3 篇文章的名称，内容和作者姓名。
SELECT a.article_title, a.content, w.writer_name FROM `platform_article` a, `platform_writer` w,
  `platform_reader` r, `platform_deal` d
WHERE a.writer_id = w.writer_id AND r.reader_id = d.reader_id AND r.reader_name = 'zoe'
  ORDER BY d.create_time LIMIT 0, 3;

# (3) 查询所有文章中付费人数最多的前 3 篇文章的名字，付费人数及总付费金额。
SELECT article_title, COUNT(deal_id) pay_nums, SUM(deal_payment) FROM `platform_article`, `platform_deal`
WHERE  platform_article.article_id = platform_deal.article_id
GROUP BY platform_deal.article_id ORDER BY pay_nums DESC LIMIT 0, 3;

# (4) 平台所有的作者姓名(platform_writer 表的 writer_name 字段)需要添加“w_”前缀，如“Joe”需要修改为“w_Joe”
UPDATE `platform_writer` SET writer_name = CONCAT('w_', writer_name);

# (5) 新创建的作者姓名仍是不带“w_”前缀的，因此需要在插入数据时自动为其添加“w_”前缀(用触发器解决，触发器的名称定义
# 为“modifywritername”)
CREATE TRIGGER modifywritername BEFORE INSERT ON `platform_writer` FOR EACH ROW
  BEGIN SET NEW.writer_name = CONCAT('w_', NEW.writer_name);
  END;

# (6) 查询每位作者的名称，该作者发表的文章总数，该作者的所有文章付费用户总数，按付费用户总数倒序排序。
SELECT w.writer_name, COUNT(DISTINCT a.article_id), COUNT(d.deal_id) pay_nums FROM `platform_writer` w
  LEFT JOIN `platform_article` a ON w.writer_id = a.writer_id LEFT JOIN `platform_deal` d
ON a.article_id = d.article_id
GROUP BY w.writer_name ORDER BY pay_nums DESC;

# (7) 创建一个试图 article_writer， 包含文章的所有字段，文章的付费总额，文章作者的姓名和邮箱
CREATE VIEW article_view(article_id, writer_id, article_title, content, create_time, writer_name, writer_email)
AS SELECT a.article_id, a.writer_id, a.article_title, a.content, a.create_time, writer_name, writer_email
   FROM `platform_article` a LEFT JOIN `platform_writer` w ON w.writer_id = a.writer_id;

# (8) 由于 create_time 是 datetime 格式，现在需要将其中的日期提取出来，查询每位读者每日的付费阅读总数和付费金额，
# 结果集中包含读者 ID，姓名，交易日期，当日付费阅读量，当日付费金额，并按照日期降序排序。
SELECT r.reader_id, r.reader_name, DATE_FORMAT(d.create_time, '%Y-%m-%d'), COUNT(d.deal_id), SUM(d.deal_payment)
FROM `platform_reader` r, `platform_deal` d
WHERE r.reader_id = d.reader_id
GROUP BY d.reader_id, DATE_FORMAT(d.create_time, '%Y-%m-%d')
ORDER BY d.create_time DESC;