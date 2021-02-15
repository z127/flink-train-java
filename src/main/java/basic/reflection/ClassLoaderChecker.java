package basic.reflection;

import connector.test;

public class ClassLoaderChecker {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        MyClassLoader m=new MyClassLoader("D:\\ideaprojects\\flink-train-java\\src\\main\\java","newCLassloader");
        Class c=m.loadClass("connector.test");
        System.out.println(c.getClassLoader());
        System.out.println(c.getClassLoader().getParent());
        System.out.println(c.getClassLoader().getParent().getParent());

        test t=(test)c.newInstance();

    }
}
