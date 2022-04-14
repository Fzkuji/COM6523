package structuralAnalysis;


import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import util.AbstractClassReader;
import util.CSVReader;
import util.ReflectionClassReader;

/**
 * Created by neilwalkinshaw on 24/10/2017.
 */
public class ClassDiagram {

    protected final String COMMA_DELIMITER = ",";
    protected Map<String,String> inheritance;
    protected Map<String,String> implementing;
    protected Map<String,Set<String>> associations;
    //include classes in the JDK etc? Can produce crowded diagrams.
    protected boolean ignoreLibraryClasses;
    protected boolean ignoreInnerClasses;
    protected String signaturePrefix = "";
    //Add additional attributes from csv to included class
    protected String additionalAttributePath = "";
    //All of the classes in the given directory structure
    protected Set<String> allClassNames;
    //All of the classes to be included in the final diagram
    protected Set<String> includedClasses;

    /**
     * Instantiating the class will populate the inheritance and association relations.
     * @param root
     */
    public ClassDiagram(String root, boolean ignoreLibs, boolean ignoreInnerClasses,
                        String signaturePrefix){
        this.ignoreLibraryClasses = ignoreLibs;
        this.signaturePrefix = signaturePrefix;
        this.ignoreInnerClasses = ignoreInnerClasses;

        File dir = new File(root);
        ReflectionClassReader rcr = new ReflectionClassReader();

        List<Class<?>> classes = rcr.processDirectory(dir,"");
        inheritance = new HashMap<String, String>();
        implementing = new HashMap<String, String>();
        associations = new HashMap<String, Set<String>>();
        allClassNames = new HashSet<String>();
        includedClasses = new HashSet<String>();

        for(Class cl : classes){
            allClassNames.add(getClassName(cl));
        }

        for(Class cl : classes){
            if(includeClass(cl))
                includedClasses.add(getClassName(cl));
        }

        for(Class cl : classes){
            extractInheritanceRelationships(cl);
            extractAssociationRelationships(cl);
        }
    }


    /**
     * For a given class cl, if it has a super-class (a parent-class)
     * add an inheritance relation from cl to the parent-class. Use the
     * 'inheritance' map to store this.
     * @param cl
     */
    private void extractInheritanceRelationships(Class cl) {
        if (!cl.isInterface()) {
            inheritance.put(getClassName(cl), getClassName(cl.getSuperclass()));
        }
        for(Class klass : cl.getInterfaces()) {
            implementing.put(getClassName(cl), getClassName(klass));
        }
        
    }

    /**
     * For a class cl, identify the fields that are declared within it.
     * Store the fields as a set of String objects, where a String refers
     * to the type of the Field (we are not interested in primitive types,
     * only in fields that correspond to other classes). Use the 'associations'
     * map to store the associations.
     * @param cl
     */
    private void extractAssociationRelationships(Class cl) {
        Set<String> fields = new HashSet<String>();
        for(Field fld : cl.getDeclaredFields()){
            //Do not want to include associations to primitive types such as ints or doubles.
            if(fld.getType() instanceof Class) {
                String toAdd =  getClassName(fld.getType());
                fields.add(toAdd);
            }
        }
        associations.put(getClassName(cl),fields);
    }

    protected boolean includeClass(Class cl){
        if(cl.getPackage()== null){
            if(!signaturePrefix.isEmpty())
                return false;
        }
        else if(!cl.getPackage().getName().startsWith(signaturePrefix))
            return false;
        if (ignoreLibraryClasses) {
            if(!allClassNames.contains(cl.getName()))
                return false;
        }

        return true;
    }

    protected String getClassName(Class cl){
        if(cl.getEnclosingClass()!=null && ignoreInnerClasses)
            return cl.getEnclosingClass().getName();
        else
            return cl.getName();
    }

    public String toString(){
        //Create dot graph head
        StringBuffer dotGraph = new StringBuffer();
        dotGraph.append("digraph classDiagram{\n" + "graph [splines=ortho, rankdir=BT, overlap = false, nodesep=2.0, ranksep=2.0]\n\n");
        dotGraph.append("node [shape=record style=filled fillcolor=gray95]\n");

        //Add nodes
        if (additionalAttributePath.isEmpty()){
            for(String className : includedClasses){
                dotGraph.append("\""+className + "\";\n");
            }
        }
        else {
            List<List<String>> withAttributeClasses = CSVReader.readCSV(additionalAttributePath);
            List<String> attributeNames = withAttributeClasses.get(0);
            withAttributeClasses.remove(0);

            Set<String> withAttributeClassSet = new HashSet<>();

            for(List<String> withAttributeClass : withAttributeClasses){
                withAttributeClassSet.add(withAttributeClass.get(0));
            }
            int attibuteNum = withAttributeClasses.get(0).size() - 1;
            for(String className : includedClasses){
                if (!withAttributeClassSet.contains(className)){
                    dotGraph.append("\""+className + "\";\n");
                }
            }
            for(List<String> withAttributeClass : withAttributeClasses){
                if (includedClasses.contains(withAttributeClass.get(0))) {
                    dotGraph.append("\"" + withAttributeClass.get(0) + "\"" + "[label = <{<b>" + withAttributeClass.get(0) + "</b>");
                    for (int i = 1; i <= attibuteNum; i ++){
                        dotGraph.append("|" + attributeNames.get(i) + ": " + withAttributeClass.get(i) + "<br/>");
                    }
                    dotGraph.append("}>]\n");
                }
            }
        }


        //Add inheritance relations
        for(String childClass : inheritance.keySet()){
            if(includedClasses.contains(childClass) && includedClasses.contains(inheritance.get(childClass))) {
                String from = "\"" + childClass + "\"";
                String to = "\"" + inheritance.get(childClass) + "\"";
                dotGraph.append(from + " -> " + to + "[arrowhead = onormal];\n");
            }
        }

        //Add implementing relations
        for(String childClass : implementing.keySet()){
            if(includedClasses.contains(childClass) && includedClasses.contains(implementing.get(childClass))) {
                String from = "\"" + childClass + "\"";
                String to = "\"" + implementing.get(childClass) + "\"";
                dotGraph.append(from + " -> " + to + "[arrowhead = curve];\n");
            }
        }

        //Add associations
        for(String cls : associations.keySet()){
            if(!includedClasses.contains(cls))
                continue;
            Set<String> fields = associations.get(cls);
            for(String field : fields) {
                if(!includedClasses.contains(field))
                    continue;
                String from = "\""+cls +"\"";
                String to = "\""+field+"\"";
                dotGraph.append(from + " -> " +to + "[arrowhead = diamond];\n");
            }
        }

        dotGraph.append("}");
        return dotGraph.toString();
    }

    /**
     * Write out the class diagram to a specified file.
     * @param target
     */
    public void writeDot(File target) throws IOException {
        BufferedWriter fw = new BufferedWriter(new FileWriter(target));
        fw.write(toString());
        fw.flush();
        fw.close();
    }

    public void getAdditionalAttributePath(String path){
        this.additionalAttributePath = path;
    }

}
