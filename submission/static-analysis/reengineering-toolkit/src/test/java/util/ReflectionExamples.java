package util;

import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionExamples {

    @Test
    public void loadClassAlreadyInClassPath() throws ClassNotFoundException {
        String targetClass = "java.util.LinkedList";

        //Here is the code that loads the class
        //NB - target class *must* be on the class path.
        Class loaded = Class.forName(targetClass);

        System.out.println("loaded class is: "+ loaded.getName());

        System.out.println("Number of methods: "+ loaded.getMethods().length);
        System.out.println("Number of fields: "+ loaded.getDeclaredFields().length);

    }

    @Test
    public void findOutSuperClass() throws ClassNotFoundException {
        String targetClass = "util.ASMClassReader";
        Class loaded = Class.forName(targetClass);

        Class superClass = loaded.getSuperclass();
        System.out.println("Parent class of + "+loaded.getName()+" is: "+ superClass.getName());

    }

    @Test
    public void findOutAssociations() throws ClassNotFoundException {
        String targetClass = "java.util.LinkedList";
        Class loaded = Class.forName(targetClass);

        System.out.println("loaded class is: "+ loaded.getName());

        Field[] fields = loaded.getDeclaredFields();
        for(int i = 0; i<fields.length; i++){
            Field current = fields[i];
            System.out.println("Field: "+ current.getName()+":"+current.getType());
        }

    }

    @Test
    public void miniClassDiagram() throws ClassNotFoundException {

        String targetClass = "java.util.Stack";

        StringBuffer graph = new StringBuffer();
        graph.append("digraph miniGraph{\n\tgraph [splines=ortho, rankdir=BT]\n");


        Class loaded = Class.forName(targetClass);
        Class superClass = loaded.getSuperclass();

        graph.append("\t\""+loaded.getName()+"\" [shape=\"box\"]\n");
        graph.append("\t\""+superClass.getName()+"\" [shape=\"box\"]\n");
        graph.append("\t\""+loaded.getName() + "\"->\""+ superClass.getName() + "\" [arrowhead=\"onormal\"]\n");

        Field[] fields = loaded.getDeclaredFields();
        Set<String> doneClasses = new HashSet<>();
        for(int i = 0; i<fields.length; i++){
            Field current = fields[i];
            String type = current.getType().getName();
            if(!doneClasses.contains(type) & !current.getType().isPrimitive()) {
                doneClasses.add(type);
                graph.append("\t\""+type+"\" [shape=\"box\"]\n");
                graph.append("\t\""+type + "\"->\"" + loaded.getName() + "\" [arrowhead=\"diamond\"]\n");
            }
        }
        graph.append("}");
        System.out.println(graph);
    }


    @Test
    public void depthLimitedClassDiagram() throws ClassNotFoundException {

        String targetClass = "java.util.Stack";

        StringBuffer graph = new StringBuffer();
        graph.append("digraph miniGraph{\n\tgraph [splines=ortho, rankdir=BT]\n");


        depthLimitedClassDiagram(targetClass,new HashSet<>(),graph,10);
        graph.append("}");
        System.out.println(graph);
    }


    private void depthLimitedClassDiagram(String targetClass, Set<String> classesIncluded, StringBuffer graph, int depth) throws ClassNotFoundException {
        if(depth<1)
            return;

        Class loaded = Class.forName(targetClass);
        Class superClass = loaded.getSuperclass();


        if(!classesIncluded.contains(loaded.getName())) {
            graph.append("\t\"" + loaded.getName() + "\" [shape=\"box\"]\n");
            classesIncluded.add(loaded.getName());
        }

        if(superClass!=null) {

            if (!classesIncluded.contains(superClass.getName())) {
                depthLimitedClassDiagram(superClass.getName(), classesIncluded, graph, depth - 1);
            }
            graph.append("\t\""+loaded.getName() + "\"->\""+ superClass.getName() + "\" [arrowhead=\"onormal\"]\n");
        }



        Field[] fields = loaded.getDeclaredFields();
        for(int i = 0; i<fields.length; i++){
            Field current = fields[i];
            String type = current.getType().getName();

            if(!classesIncluded.contains(type)& !current.getType().isPrimitive()){
                depthLimitedClassDiagram(type,classesIncluded,graph,depth-1);

            }
            if(!current.getType().isPrimitive()){
                graph.append("\t\""+type + "\"->\"" + loaded.getName() + "\" [arrowhead=\"diamond\"]\n");
            }
        }
    }



}
