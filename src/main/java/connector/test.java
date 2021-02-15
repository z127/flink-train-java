package connector;

public class test {
    static {
        System.out.println("触发static方法了");
    }
    public static void main(String[] args) {
        int i=1;
        int j=1;
        i++;
        ++j;
        System.out.println(i);
        System.out.println(j);
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));

    }

    public  void hello(){
        System.out.println("class loader 被调用了");
    }
}

