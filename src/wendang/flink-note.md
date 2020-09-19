# Standalone-分布式方式
    - 1.Java 1.8.x or higher
    - 2.ssh 多个机器之间要互通
        每个机器需要添加ip和hostname的映射关系

    JDK 
    Flink同一个目录下
    - 3.配置conf
        flink-1.7.0/conf/flink-conf.yaml
        jobmanager.rpc.address: 10.0.0.1 配置主节点的ip
        jobmanager 主节点
        taskmanager 从节点
        flink/conf/workers
        每一行配置一个ip/host 
    - 4.常用配置
        jobmanager.rpc.address master节点的地址
        jobmanager.heap.mb jobmanager节点可用的内存
        taskmanager.heap.mb taskmanager节点可用的内存
        taskmanager.numberofTaskSlots 每个机器可用的CPU个数
        parallelism.default 任务的并行度
        taskmanager.tmp.dirs taskmanager的临时数据存储目录
    - 5.扩展和容错

ON YARN是企业级用的最多的方式 ******

1.第一种方式 ，只开一个flink-session，多个application共享一个session
flink/bin/yarn-session.sh -n 1 -jm 1024m -tm 1024m
-n taskmanager的数量
-jm jobmanager的内存
-tm taskmanager的内存

命令是错的
hadoop fs -copyFromLocal LICENSE-2.0.txt hdfs:///s200:8020
这个才是正确命令
hadoop fs -copyFromLocal ./LICENSE-2.0.txt hdfs://s200:9000

flink/bin/flink run ./examples/batch/WordCount.jar \
       --input hdfs://s200:9000/LICENSE-2.0.txt --output hdfs://s200:9000/wordcount-result.txt


yarn  application -kill application_1598691099764_0004


2.第二种方式,单独申请一个flink-session，多个application多个session


./bin/flink run -m yarn-cluster -yn 1 ./examples/batch/WordCount.jar


查看相关log 

yarn logs -applicationId <application ID>


作业:
快速开发一个flink应用程序
    批处理&流处理

    out of the box 开箱即用


# kakfa安装

## 安装Zookeeper
 1. http://archive.cloudera.com/cdh5/cdh/5/zookeeper-3.4.5-cdh5.15.1.tar.gz，解压文件到app目录

 2. 配置环境变量 ~/etc/profice

 3. 配置文件 $ZK_HOME/conf.zoo.cfg 
 cp zoo_sample.cfg zoo.cfg
 dataDir不要放在默认的/tmp中，默认为
  dataDir=/home/hadoop/used_data/temp/zookeeper
 4. 启动zk，$ZK_HOME/bin/zkServer.sh start
 5. 检查是否启动成功 jps,发现QuorumPeerMain 代表启动成功


## 安装kafka
1.下载kafka_2.11-1.1.1.tgz scala=2.11
2.cdh5.15.1.tar.gz，解压文件到app目录

 1. 配置环境变量 ~/etc/profice
 2. 配置文件 $KAFKA_HOME/config/server.properties
 修改为
 zookeeper.connect=s200:2181
 log.dirs=/home/hadoop/used_data/temp/kafka-logs
 3. 启动kafka
 $KAFKA_HOME/bin/kafka-server-start.sh -daemon /home/hadoop/app/kafka_2.11-1.1.1/config/server.properties
 4. jps检查是否成功 
 _有问题去kafka里面的logs_

5. 创建topic
 $KAFKA_HOME/bin/kafka-topics.sh --create --zookeeper s200:2181 --replication-factor 1 --partitions 1 --topic zqjtest
6. 查下看topic
 $KAFKA_HOME/bin/kafka-topics.sh --list --zookeeper s200:2181
7. 启动生产者
 $KAFKA_HOME/bin/kafka-console-producer.sh  --broker-list s200:9092 --topic zqjtest
8. 启动消费者
$KAFKA_HOME/bin/kafka-console-consumer.sh  --bootstrap-server s200:9092 --topic zqjtest

9. 查看kafka全部数据
$KAFKA_HOME/bin/kafka-console-consumer.sh  --bootstrap-server s200:9092 --topic zqjtest --from-beginning

10.查看kafka全部topic
./kafka-topics.sh --list --zookeeper localhost:2181

11.kafka数据清理策略

1 log.retention.hours=1　　　　　　　
- 超过1个小时就清理数据

2 log.segment.bytes=5000　　　　
- 数据量超过5000byte就清理数据 

3 log.cleanup.interval.mins=100　
- 指定日志每隔多久检查看是否可以被删除，默认1分钟

4 log.retention.check.interval.ms=300　　 
- 文件大小检查的周期时间，是否触发 log.cleanup.policy中设置的策略

9.kafka session报错 是因为没有配置数据
在 /home/hadoop/app/kafka_2.11-1.1.1/config/server.properties中需要配置一个

添加一个host.name,配置advertised.listeners=PLAINTEXT://39.108.61.252:9092，注意你的kafka集群中都应该配置有效的advertised.listeners值，否则集群间不能通信


'''
    ############################# Socket Server Settings #############################
    host.name=39.108.61.252

    # The address the socket server listens on. It will get the value returned from
    # java.net.InetAddress.getCanonicalHostName() if not configured.
    #   FORMAT:
    #     listeners = listener_name://host_name:port
    #   EXAMPLE:
    #     listeners = PLAINTEXT://your.host.name:9092
    listeners=PLAINTEXT://:9092


    # Hostname and port the broker will advertise to producers and consumers. If not set,
    # it uses the value for "listeners" if configured.  Otherwise, it will use the value
    # returned from java.net.InetAddress.getCanonicalHostName().
    advertised.listeners=PLAINTEXT://39.108.61.252:9092
'''