package basic.reflection;

public class Robot {
    private  String name;
    static {
        System.out.println("robot 的静态代码块生成了");
    }
    public  void printName(String name)
    {
        System.out.println(name+" "+this.name);
    }

    private  String helloPerson(String id)
    {
        return  "Hello "+id;
    }

}
