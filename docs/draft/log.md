

# 日志监控
使用过滤器com.alibaba.druid.support.http.WebStatFilter;
* **在log4j那边控制日志级别输出 org.apache.log4j.Category**

* 启动类StartListener

1. 抓取jsp Runtime Exception
2. 抓取java Runtme Exception
3. 抓取SQL Exception
> 1. 方式一:FilterChainImpl存在sql的地方
> 2. JFinal
4. 调用存过的日志
5. 去掉e.printStrace()和log.error，替换成...
6. 修改log4j的info和error输出?


# 优化日志输出
1. 一个请求多条相同(参数不同)合并一条，去掉参数并合并AccessAnalysis(110)
> 测试页:http://localhost:8080/RedseaPlatform/Message.mc?method=getOrgUsers
2. 异常打印过多，cause过多，打印部分日志
> 测试页:在bean中的getxxx产生个异常，MongodbService(82)
3. 去掉多余的空格（含回车）
> 测试页:花名册 sql.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "")


# 注意事项
1. 注意定时任务时，调用出现的空指针




# 其它
1. SQL参数读取
```
at com.mysql.jdbc.PreparedStatement.setTimestamp(PreparedStatement.java:4262)
at com.alibaba.druid.filter.FilterChainImpl.preparedStatement_setTimestamp(FilterChainImpl.java:2866)
at com.alibaba.druid.filter.FilterAdapter.preparedStatement_setTimestamp(FilterAdapter.java:1358)
at com.alibaba.druid.filter.FilterChainImpl.preparedStatement_setTimestamp(FilterChainImpl.java:2863)
at com.alibaba.druid.proxy.jdbc.PreparedStatementProxyImpl.setTimestamp(PreparedStatementProxyImpl.java:581)
at com.alibaba.druid.pool.DruidPooledPreparedStatement.setTimestamp(DruidPooledPreparedStatement.java:409)
at org.springframework.jdbc.core.StatementCreatorUtils.setValue(StatementCreatorUtils.java:393)
at org.springframework.jdbc.core.StatementCreatorUtils.setParameterValueInternal(StatementCreatorUtils.java:234)
at org.springframework.jdbc.core.StatementCreatorUtils.setParameterValue(StatementCreatorUtils.java:165)
at org.springframework.jdbc.core.ArgumentPreparedStatementSetter.doSetValue(ArgumentPreparedStatementSetter.java:65)
at org.springframework.jdbc.core.ArgumentPreparedStatementSetter.setValues(ArgumentPreparedStatementSetter.java:46)
at org.springframework.jdbc.core.JdbcTemplate$1.doInPreparedStatement(JdbcTemplate.java:644)
at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:589)
```






