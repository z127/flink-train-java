package dataset;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;



public class FirstFunction {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //fromCollection(env);
        //filterFunction(env);
    firstFunction(env);
    }

    public  static  void firstFunction(ExecutionEnvironment env) throws Exception {
        List<Tuple2<Integer,String>> info=new ArrayList<Tuple2<Integer,String>>();
        info.add(new Tuple2(1,"hadoop"));
        info.add(new Tuple2(1,"spark"));
        info.add(new Tuple2(1,"flink"));
        info.add(new Tuple2(2,"Java"));
        info.add(new Tuple2(2,"Spring Boot"));
        info.add(new Tuple2(3,"Linux"));
        info.add(new Tuple2(4,"Vue"));
        DataSource<Tuple2<Integer,String>> data= env.fromCollection(info);
        data.first(3).print();
        System.out.println("------------------------");
        data.groupBy(0).first(2).print();
        System.out.println("------------------------");
        data.groupBy(0).sortGroup(1, Order.DESCENDING).first(2).print();

    }




}
