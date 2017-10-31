# 1. 某服装店的后台管理系统的部分关系模式如下：
# 服装表 clothes(cid, name, price, brand, type, launchYear)
# 订单表 order(oid, cuid, cid, quantity, totalPrice, orderTime)
# 顾客表 customer(cuid, cname, phone)
# 其中:
# brand 为服装品牌，type 为服装种类，launchYear 为上市年份，year 类型；
# orderTime 为下单时间，dateTime 类型
# 与时间比较相关的 mysql 函数：date_format, date_sub

# 1. 请创建表 order（要求：oid 为主键，其余子段为不能为空）
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `oid` int NOT NULL AUTO_INCREMENT,
  `cuid` int NOT NULL,
  `cid` int NOT NULL,
  `quantity` int NOT NULL,
  `totalPrice` double NOT NULL,
  `orderTime` DATETIME NOT NULL,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# 2. 查询单价在 120 元到 180 元之间（包括 120 元和 150 元）的所有衬衫，列出它们的名字和单价，并按照价格递增。
SELECT name, price FROM `clothes`
WHERE price >= 120 AND price <= 180 AND type = '衬衫'
ORDER BY price ASC;

# 3. 查询【nike】2015 年新上市的所有【裤子】至今为止的各自的销量。
SELECT clothes.name, IFNULL(SUM(order.quantity), 0) FROM `clothes`
  LEFT JOIN `order` ON clothes.cid = order.cid
WHERE brand = 'nike' AND type = '裤子' AND launchYear = 2015
GROUP BY clothes.cid;

# 4. 查询顾客【jacky】在 2014 年 11 月这个月内购买服装所花的总费用。
SELECT SUM(totalPrice) FROM `order`
WHERE DATE_FORMAT(ordertime, '%Y-%m-%d') BETWEEN '2014-11-01' and '2014-11-30' AND cuid IN (
  SELECT cuid FROM customer WHERE cname = 'jacky');

# 5. 查询同时购买了【nike】品牌【2015】年新上市的最贵的【外套】和【裤子】的顾客的【姓名】。 ??
SELECT cname FROM `customer`
WHERE cuid IN (
  SELECT cuid FROM `order` WHERE cid IN (
    SELECT cid FROM `clothes`
    WHERE brand = 'nike' AND type = '外套' AND price = (
      SELECT MAX(price) from clothes
      WHERE brand = 'nike' and type = '外套'))
  AND cid IN (
    SELECT cid FROM `clothes`
    WHERE brand = 'nike' AND type = '裤子' AND price = (
      SELECT MAX(price) from clothes
      WHERE brand = 'nike' and type = '裤子'))
GROUP BY cuid HAVING COUNT(DISTINCT cid) = 2);

# 6. 查询 2014.12.12 这天销量排名【前三】的服装的【名称】，【销量】以及它们对应的【品牌】。
SELECT clothes.name, SUM(order.quantity), clothes.brand FROM `clothes`, `order`
WHERE clothes.cid = order.cid AND DATE_FORMAT(orderTime, '%Y-%m-%d') = '2014-12-12'
GROUP BY order.cid
HAVING sum(order.quantity) >= (
  SELECT SUM(o2.quantity) FROM `order` o2 WHERE DATE_FORMAT(orderTime, '%Y-%m-%d') = '2014-12-12'
  GROUP BY o2.cid ORDER BY SUM(o2.quantity) DESC LIMIT 2, 1)
ORDER BY SUM(order.quantity) DESC;

# 7. 查询 2014.11.11，在所有购买了 nike 品牌服装的顾客中，消费金额最大的顾客的姓名和联系电话。 ??
SELECT cu.cname, cu.phone FROM `customer` cu
WHERE EXISTS (
  SELECT SUM(o.totalPrice) FROM `order` o, `clothes` c
  WHERE cu.cuid = o.cuid AND o.cid = c.cid AND c.brand = 'nike' AND
  DATE_FORMAT(o.orderTime, '%Y-%m-%d') = '2014-11-11'
  GROUP BY o.cuid HAVING SUM(o.totalPrice) = (
    SELECT o2.totalPrice FROM `order` o2, `clothes` c
    WHERE o2.cid = c.cid AND c.brand = 'nike' AND DATE_FORMAT(o2.orderTime, '%Y-%m-%d') = '2014-11-11'
    GROUP BY o2.cuid ORDER BY SUM(o2.totalprice) DESC LIMIT 1));

# 8. 查询 2014.12.12 这天，每个订单消费金额都在 800 元及以上的顾客的信息。
SELECT DISTINCT customer.cuid, customer.cname, customer.phone FROM `customer`, `order`
WHERE customer.cuid = order.cuid AND DATE_FORMAT(orderTime, '%Y-%m-%d') = '2014-12-12' AND
  customer.cuid NOT IN (
    SELECT o2.cuid FROM `order` o2
    WHERE DATE_FORMAT(orderTime, '%Y-%m-%d') = '2014-12-12' AND o2.totalPrice < 800);

# 9. 删除 2015 年 9 月 1 日之前过去一年内没有消费过的顾客的信息。
DELETE FROM `customer`
WHERE cuid NOT IN (
  SELECT cuid FROM `order`
  WHERE DATE_FORMAT(ordertime, '%Y-%m-%d') < '2015-09-01' AND
    ordertime >= DATE_SUB(DATE_FORMAT('2015-09-01', INTERVAL 1 YEAR))
);

# 10. 授予销售经理的账号 Mike 对表 customer 的更新、插入和查询权限，但不给删除权限。
GRANT UPDATE, INSERT, SELECT ON customer TO 'Mike';