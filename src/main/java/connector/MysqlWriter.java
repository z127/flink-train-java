package connector;

import com.peter.flink.java.entity.House;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlWriter extends RichSinkFunction<Tuple3<String,House, Integer>> {
    private Connection connection;
    private PreparedStatement preparedStatement;
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // 加载JDBC驱动
        Class.forName("com.mysql.jdbc.Driver");//加载数据库驱动
        connection = DriverManager.getConnection("jdbc:mysql://47.114.46.32:3306/VueTomShop?useUnicode=true&characterEncoding=utf-8", "root", "123456");//获取连接
        //replace into tom_test.dw_house_info values(,?,?,?,?,?,?,?,?,?,?,?,?)
        String sql="replace into tom_test.dw_house_info values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
    public void invoke(Tuple3<String, House, Integer> value, Context context) throws Exception {
        try {
            House house = (House)value.getField(1);//获取JdbcReader发送过来的结果
            convert_to_mysql(house);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void convert_to_mysql(House house) throws SQLException {
        preparedStatement.setString(1,house.getDs());
        preparedStatement.setString(2,house.getTs());
        preparedStatement.setString(3,house.getDescription());
        preparedStatement.setString(4,house.getName());
        preparedStatement.setString(5,house.getSize());
        preparedStatement.setString(6,house.getDirection());
        preparedStatement.setString(7,house.getDecoration());
        preparedStatement.setString(8,house.getFloor());
        preparedStatement.setString(9,house.getCategory());
        preparedStatement.setString(10,house.getTotal_price());
        preparedStatement.setString(11,house.getAvg_price());
        preparedStatement.setString(12,house.getAdvantage());
        preparedStatement.setString(13,house.getHid());

    }
}