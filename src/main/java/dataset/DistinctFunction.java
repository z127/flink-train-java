package dataset;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class DistinctFunction {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        distinctExampleFunction(env);
    }


    public  static  void distinctExampleFunction(ExecutionEnvironment env) throws Exception {
        List<String> info = new ArrayList<String>();
        info.add("hadoop,spark");
        info.add("hadoop,spark");
        info.add("hadoop,flink");
        info.add("scala,flink");
        DataSource<String> data = env.fromCollection(info);
        data.flatMap(new FlatMapFunction<String, String>() {

            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] inputs = value.split(",");
                if (inputs.length > 0) {
                    for (String ele : inputs) {
                        out.collect(ele);
                    }
                }
            }
        }).distinct().print();
    }
}



