package connector;


import com.peter.flink.java.entity.House;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MysqlSource extends RichSourceFunction<Tuple2<String, String>> {
    private static final Logger logger = LoggerFactory.getLogger(MysqlSource.class);

    private Connection connection = null;
    private PreparedStatement ps = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        Class.forName("com.mysql.jdbc.Driver");//加载数据库驱动
        connection = DriverManager.getConnection("jdbc:mysql://47.114.46.32:3306/VueTomShop?useUnicode=true&characterEncoding=utf-8", "root", "123456");//获取连接
        ps = connection.prepareStatement("select * from VueTomShop.goods_goodscategory");
    }

    @Override
    public void run(SourceContext<Tuple2<String, String>> ctx) throws Exception {
        try {
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
//                House vo = new House();
//                vo.setHid(resultSet.getString("Hid"));
//                ctx.collect(vo);
                String name = resultSet.getString("name");
                String id = resultSet.getString("add_time");
                logger.error("readJDBC name:{}", name);
                Tuple2<String,String> tuple2 = new Tuple2<>();
                tuple2.setFields(id,name);
                ctx.collect(tuple2);//发送结果，结果是tuple2类型，2表示两个元素，可根据实际情况选择
            }
        } catch (Exception e) {
            logger.error("runException:{}", e);
        }
    }

    @Override
    public void cancel() {
        try {
            super.close();
            if (connection != null) {
                connection.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            logger.error("runException:{}", e);
        }
    }
}
