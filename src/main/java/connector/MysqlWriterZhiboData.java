package connector;

import com.peter.flink.java.entity.House;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MysqlWriterZhiboData extends RichSinkFunction<Tuple3<String,String, Long>> {
    private Connection connection;
    private PreparedStatement preparedStatement;
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // 加载JDBC驱动
        Class.forName("com.mysql.jdbc.Driver");//加载数据库驱动
        connection = DriverManager.getConnection("jdbc:mysql://47.114.46.32:3306/flink_data_zhibo?useUnicode=true&characterEncoding=utf-8", "root", "123456");//获取连接
        //replace into tom_test.dw_house_info values(,?,?,?,?,?,?,?,?,?,?,?,?)
        String sql="replace into computed_data values(?,?,?)";
        preparedStatement = connection.prepareStatement(sql);//insert sql在配置文件中
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
        if(preparedStatement != null){
            preparedStatement.close();
        }
        if(connection != null){
            connection.close();
        }
        super.close();
    }

    @Override
    public void invoke(Tuple3<String, String, Long> value, Context context) throws Exception {
        try {
            System.out.println("f0"+value.f0);
          Long  time_stamp_cnt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.f0).getTime();
            System.out.println(value);
//            preparedStatement.setTimestamp(1, new  java.sql.Timestamp(time_stamp_cnt));
            preparedStatement.setString(1, new  java.sql.Timestamp(time_stamp_cnt).toString());
            preparedStatement.setString(2,value.f1);
            preparedStatement.setLong(3,value.f2);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}