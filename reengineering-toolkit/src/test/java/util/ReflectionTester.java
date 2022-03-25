package util;

import org.junit.Test;
import util.ReflectionClassReader;

import java.io.File;
import java.util.List;

public class ReflectionTester {

    @Test
    public void reflectionDemo() throws ClassNotFoundException {
        String targetClass = "util.ASMClassReader";
        Class loaded = Class.forName(targetClass);
        System.out.println("Class is: "+ loaded.getName());
        System.out.println("Super class is: "+ loaded.getSuperclass());
    }

    @Test
    public void loadDirectoryOfClasses(){
        ReflectionClassReader rcr = new ReflectionClassReader();
        List<Class<?>> loaded = rcr.processDirectory(new File("target/classes"),"");
        for(Class<?> cl : loaded){
            System.out.println("loaded: "+cl.getName());
        }

    }

}
