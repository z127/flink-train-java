package dataset;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class JavaDataSetDataSourceApp {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //fromCollection(env);
        //filterFunction(env);
        mapPartitionFunction(env);
    }

    public  static  void mapPartitionFunction(ExecutionEnvironment env) throws Exception
    {
        List<String> list=new ArrayList<String>();
        for(int i=0;i<=100;i++)
        {
            list.add("student: "+i);
        }
        DataSource<String> data=env.fromCollection(list).setParallelism(3);
//        data.map(new MapFunction<String, String>() {
//            public  String map(String input) throws  Exception{
//                String connection =DBUtils.getConnection();
//                System.out.println("connection = ["+ connection+"]");
//                DBUtils.returnConnecion(connection);
//                return  input;
//            }
//        }).print();
        data.mapPartition(new MapPartitionFunction<String, String>() {
            @Override
            public void mapPartition(Iterable<String> input, Collector<String> collector) throws Exception {
                        String connection=DBUtils.getConnection();
                        System.out.println("connection = ["+ connection+"]");
                        DBUtils.returnConnecion(connection);
            }
        }).print();

    }

    public  static  void fromCollection(ExecutionEnvironment env) throws Exception {
        List<Integer> list=new ArrayList<Integer>();
        for(int i=0;i<=10;i++)
        {
            list.add(i);
        }
        env.fromCollection(list).print();
    }

    public  static void filterFunction (ExecutionEnvironment env) throws Exception {
        List<Integer> list=new ArrayList<Integer>();
        for(int i=0;i<=10;i++)
        {
            list.add(i);
        }
        DataSource<Integer> data = env.fromCollection(list);
        data.map(new MapFunction<Integer, Integer>() {
            public Integer map(Integer input) throws Exception
            {
                return  input+1;
            }
        }).filter(new FilterFunction<Integer>() {
            @Override
            public boolean filter(Integer value) throws Exception {
                return value>5;
            }
        }).print();

    }


}
