package dataset;

import java.util.Random;

public class DBUtils {
    public static String getConnection()
    {
        return new Random().nextInt(10)+"";
    }

    public  static void returnConnecion(String connection)
    {

    }

}
