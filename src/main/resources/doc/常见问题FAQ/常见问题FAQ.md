title: 常见问题FAQ updateAt: 2023-09-28 10:00:00 ---

## 1. 同步任务状态正常，但是没有进行同步
以下几个方面检查一下：
1.	源端为AntDB-T的用户的角色必须要有LOGIN和REPLICATION的权限，建议赋超级用户权限。
2.	源端和目标端的表必须要有主键。
3.	确认Kafka、Zookeeper等中间件是否运行正常。
## 2. ElasticSearch支持哪些版本
目前只支持ElasticSearch版本7。其他版本因为客户端的解析导致数据存入错误。测试选择版本：7.17.4。
如果需要支持其他版本的ElasticSearch，请联系项目管理员。
## 3. 源端执行增减字段或者变更主键是否支持
目前都不支持。
## 4. 目标端的运行指标为空
除ElasticSearch外，其余目标端类型已支持输出运行指标。
## 5. 目前不支持的字段类型有哪些
有部分数据类型是不支持的，部分是支持但是在转换时可能会丢失精度。

|数据库类型	|字段类型|	备注|
|----|----|----|
|AntDB-T|	DECIMAL<br>NUMERIC<br>MONEY<br>DOUBLE PRECISION	|在精度超出15位时会丢失精度|
||	BIT<br>BIT VARYING<br>一维以上数组|	不支持|
|MySQL	|GEOMETRY<br>POINT<br>LINESTRING<br>POLYGON<br>MULTIPOINT<br>MULTILINESTRING<br>MULTIPOLYGON<br>GEOMETRYCOLLECTION	|不支持|
|ORACLE	|INTERVAL YEAR TO MONTH<br>INTERVAL DAY TO SECOND<br>BFILE	|不支持|
||	TIMESTAMP<br>TIMESTAMP WITH TIME ZONE<br>TIMESTAMP WITH LOCAL TIME ZONE|	目标端为AntDB-T或者MySQL，在精度大于6时（7、8、9）会丢失，也就是不支持纳秒。这是目标端数据库决定的。|
## 6. 目标端为AntDB-T遇到的几个问题
1.	insert into ... ON CONFLICT DO UPDATE SET的语法在特定的时机上会出现 NOT SUPPORT。该问题导致目标端为AntDB-T时，增量同步报错。
http://10.20.16.216:9090/ADB/AntDB/-/issues/1825
临时解决方案：新增或调整connect-distributed.properties中如下配置：
consumer.max.poll.records=100【配置往小了调，需要多次尝试以找到一个合适的值，默认500，所以调整后会影响性能】
2.	AntDB-42.2.26.jre8.jar驱动在replacation模式下出现语法错误。该问题导致源端为AntDB-T时，无法新建复制槽和发布。
http://10.20.16.216:9090/ADB/AntDB/-/issues/1916
3.	在Oracle兼容模式下，字段类型为nvarchar2、nchar，无法插入NULL值，报“Unknown Types value”
http://10.20.16.216:9090/ADB/AntDB/-/issues/2127
## 7. Kafka topic过多，导致文件句柄数超限
config/application.properties中的源端模板将"transforms":"t0",改为"transforms":"t0,merge"，增加"transforms.merge.type":"com.ai.dbsync.transform.TopicMergeTransform"。
## 8. 如何在Kafka中事先创建好topic
有时，遇到使用客户提供的Kafka集群时，可能要求事先建立好topic，而不能工具自己生成。
当前迁移工具使用的topic分为两类，一类是固定名称创建的，一类是根据任务编号创建的，由于任务编号的不确定性，所以新增了一个开关来控制topic名称根据任务名来创建。
在配置文件config/application.properties新增或调整为use.flow.name.as.topic.name=true，具体各个topic参数如下表。

|分类|	名字|	分区|	副本数	|清理策略	|用途|
|----|----|----|----|----|----|
|固定topic	|connect-configs|	1	|默认|	compact	|任务配置信息|
	||connect-offsets	|25	|默认	|compact|	任务的断点信息|
	||connect-status	|5	|默认	|compact	|任务状态|
	||kafkasql-journal	|1	|默认	|delete	|数据序列化结构定义|
|任务topic	|流程名	|24	|默认	|delete	|放表结构|
	||流程名.antcdc	|24	|默认	|delete	|放数据|
	||sync.h.流程名	|1	|默认	|delete	|记录断点对应的表结构|
## 9. 目标端为AntDB-T，如何处理（0x00）字符
Oracle和MySQL支持存储（0x00）字符，但是AntDB-T会此报错ERROR: invalid byte sequence for encoding "UTF8": 0x00。
有两种方式可以处理：
1.	AntDB-T的字段类型改为bytea，需要业务侧考虑影响；
2.	AntCDC支持使用replaceAll将0x00即\u0000替换为空字符串，需要在配置文件config/application.properties新增或调整为replace.zero.char=true，重启管理工具使配置生效，任务需要重新配置。
在AntCDC V2.1版本中，已经支持在配置迁移任务时通过设置高级选项来使上述第二种方式生效。

![在高级选项中设置是否替换0x00字符](../../images/在高级选项中设置是否替换0x00字符.png)

## 10. 如何更换为使用方提供的带验证的Kafka集群
1.	需要在配置文件config/connect-distributed.properties新增如下配置，并重启kafka connect使配置生效：

a)	修改bootstrap.servers参数：
`vim rel/antcdc/config/connect-distributed.properties
bootstrap.servers=192.168.10.146:9092,192.168.10.148:9092,192.168.10.149:9092`

b)	PLAIN模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：
`security.protocol=SASL_PLAINTEXT
sasl.mechanism=PLAIN
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="name" password="pwd";`

`producer.security.protocol=SASL_PLAINTEXT
producer.sasl.mechanism=PLAIN
producer.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="name" password="pwd";`

`consumer.security.protocol=SASL_PLAINTEXT
consumer.sasl.mechanism=PLAIN
consumer.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="name" password="pwd";`

c)	GSSAPI模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：
`security.protocol=SASL_PLAINTEXT
sasl.mechanism=GSSAPI
sasl.kerberos.service.name=kafka
sasl.jaas.config=com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true storeKey=true keyTab="/etc/security/keytabs/kafka_client.keytab" principal="connect@EXAMPLE.COM";`

`producer.security.protocol=SASL_PLAINTEXT
producer.sasl.mechanism=GSSAPI
producer.sasl.kerberos.service.name=kafka
producer.sasl.jaas.config=com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true storeKey=true keyTab="/etc/security/keytabs/kafka_client.keytab" principal="connect@EXAMPLE.COM";`

`consumer.security.protocol=SASL_PLAINTEXT
consumer.sasl.mechanism=GSSAPI
consumer.sasl.kerberos.service.name=kafka
consumer.sasl.jaas.config=com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true storeKey=true keyTab="/etc/security/keytabs/kafka_client.keytab" principal="connect@EXAMPLE.COM";`

d)	SCRAM模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：

`security.protocol=SASL_PLAINTEXT
sasl.mechanism=SCRAM-SHA-256
sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="name" password="pwd";`

`producer.security.protocol=SASL_PLAINTEXT
producer.sasl.mechanism=SCRAM-SHA-256
producer.sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="name" password="pwd";`

`consumer.security.protocol=SASL_PLAINTEXT
consumer.sasl.mechanism=SCRAM-SHA-256
consumer.sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="name" password="pwd";`

2.	将配置样例里的kafka.security.properties拷贝到配置文件夹下，修改配置，并重启web服务使配置生效：

a)	修改kafka.bootstrap.servers参数：

`cp rel/antcdc/config.example/kafka.security.properties rel/antcdc/config`

`vim rel/antcdc/config/kafka.security.properties`

`kafka.bootstrap.servers=192.168.10.146:9092,192.168.10.148:9092,192.168.10.149:9092`

b)	PLAIN模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：
`security.protocol=SASL_PLAINTEXT
sasl.mechanism=PLAIN
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required  username="name"  password="pwd";`

c)	GSSAPI模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：
`security.protocol=SASL_PLAINTEXT
sasl.mechanism=GSSAPI
sasl.kerberos.service.name=kafka
sasl.jaas.config=com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true storeKey=true keyTab="/etc/security/keytabs/kafka_client.keytab" principal="connect@EXAMPLE.COM";`

d)	SCRAM模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：
`security.protocol=SASL_PLAINTEXT
sasl.mechanism=SCRAM-SHA-256
sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="name" password="pwd";`

3.	在config/application.properties中的源端模板增加如下配置：

a)	PLAIN模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：

`"database.history.security.protocol":"SASL_PLAINTEXT",\
"database.history.sasl.mechanism":"PLAIN",\
"database.history.sasl.jaas.config":"org.apache.kafka.common.security.plain.PlainLoginModule required  username=\\\"name\\\"  password=\\\"pwd\\\";",\
"database.history.producer.security.protocol":"SASL_PLAINTEXT",\
"database.history.producer.sasl.mechanism":"PLAIN",\
"database.history.producer.sasl.jaas.config":"org.apache.kafka.common.security.plain.PlainLoginModule required  username=\\\"name\\\"  password=\\\"pwd\\\";",\
"database.history.consumer.security.protocol":"SASL_PLAINTEXT",\
"database.history.consumer.sasl.mechanism":"PLAIN",\
"database.history.consumer.sasl.jaas.config":"org.apache.kafka.common.security.plain.PlainLoginModule required  username=\\\"name\\\"  password=\\\"pwd\\\";",\`

b)	GSSAPI模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：

`"database.history.security.protocol":"SASL_PLAINTEXT",\
"database.history.sasl.mechanism":"GSSAPI",\
"database.history.sasl.kerberos.service.name":"kafka",\
"database.history.sasl.jaas.config":"com.sun.security.auth.module.Krb5LoginModule required  useKeyTab=true  storeKey=true  keyTab=\\\"/etc/security/keytabs/kafka_client.keytab\\\"  principal=\\\"connect@EXAMPLE.COM\\\";",\
"database.history.producer.security.protocol":"SASL_PLAINTEXT",\
"database.history.producer.sasl.mechanism":"GSSAPI",\
"database.history.producer.sasl.kerberos.service.name":"kafka",\
"database.history.producer.sasl.jaas.config":"com.sun.security.auth.module.Krb5LoginModule required  useKeyTab=true  storeKey=true  keyTab=\\\"/etc/security/keytabs/kafka_client.keytab\\\"  principal=\\\"connect@EXAMPLE.COM\\\";",\
"database.history.consumer.security.protocol":"SASL_PLAINTEXT",\
"database.history.consumer.sasl.mechanism":"GSSAPI",\
"database.history.consumer.sasl.kerberos.service.name":"kafka",\
"database.history.consumer.sasl.jaas.config":"com.sun.security.auth.module.Krb5LoginModule required  useKeyTab=true  storeKey=true  keyTab=\\\"/etc/security/keytabs/kafka_client.keytab\\\" principal=\\\"connect@EXAMPLE.COM\\\";",\`

c)	SCRAM模式，如果开启SSL，需要把security.protocol修改为SASL_SSL：

`"database.history.security.protocol":"SASL_PLAINTEXT",\
"database.history.sasl.mechanism":"SCRAM-SHA-256",\
"database.history.sasl.jaas.config":"org.apache.kafka.common.security.scram.ScramLoginModule required  username=\\\"name\\\"  password=\\\"pwd\\\";",\
"database.history.producer.security.protocol":"SASL_PLAINTEXT",\
"database.history.producer.sasl.mechanism":"SCRAM-SHA-256",\
"database.history.producer.sasl.jaas.config":"org.apache.kafka.common.security.scram.ScramLoginModule required  username=\\\"name\\\"  password=\\\"pwd\\\";",\
"database.history.consumer.security.protocol":"SASL_PLAINTEXT",\
"database.history.consumer.sasl.mechanism":"SCRAM-SHA-256",\
"database.history.consumer.sasl.jaas.config":"org.apache.kafka.common.security.scram.ScramLoginModule required  username=\\\"name\\\"  password=\\\"pwd\\\";",\`

## 11. 源端为AntDB-T，如何手动创建复制槽和发布
AntCDC管理页面自带创建复制槽和发布，但在生产环境，可能需要DBA手工去创建，可以参考如下命令：
`SELECT * FROM PG_CREATE_LOGICAL_REPLICATION_SLOT('name','pgoutput');`

`CREATE PUBLICATION name FOR TABLE table_name1,table_name2;`

不建议使用：
`CREATE PUBLICATION name FOR ALL TABLES;`

## 12. 源端为Oracle，DBA不赋锁表权限
config/application.properties中的源端模板增加"snapshot.locking.mode":"none"，重启前台管理服务使生效。但是无锁同步会影响数据一致性,从精准一次性变成最少一次。
