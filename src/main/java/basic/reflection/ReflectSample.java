package basic.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectSample {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Class clazz=Class.forName("basic.reflection.Robot");
        Robot   r=(Robot)clazz.newInstance();
        System.out.println("class name is "+clazz.getName());
        Method getHello=clazz.getDeclaredMethod("helloPerson",String.class);
        getHello.setAccessible(true);
        Object str=getHello.invoke(r,"Bob");
        System.out.println("getHello result is " +str);
        Method printName=clazz.getMethod("printName",String.class);
        printName.invoke(r,"wangming");
        Field name=clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(r,"Alice");
        printName.setAccessible(true);
        printName.invoke(r,"welcome");
    }
}
