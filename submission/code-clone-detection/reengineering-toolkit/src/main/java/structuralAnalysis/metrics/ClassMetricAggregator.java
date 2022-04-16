package structuralAnalysis.metrics;

import dependenceAnalysis.interprocedural.ClassCallGraph;
import org.objectweb.asm.tree.ClassNode;
import structuralAnalysis.metrics.clazz.CouplingBetweenObjects;
import structuralAnalysis.metrics.clazz.DepthOfInheritanceTree;
import structuralAnalysis.metrics.clazz.WeightedMethodCount;
import util.ASMClassReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassMetricAggregator {

    Map<ClassNode, Metric<ClassNode>> cbo,dit,wmc;
    List<ClassNode> classes;

    public ClassMetricAggregator(String root){

        File rootFile = new File(root);

        cbo = new HashMap<>();
        dit = new HashMap<>();
        wmc = new HashMap<>();

        ASMClassReader asmr = new ASMClassReader();

        classes = asmr.processDirectory(rootFile,"");

        ClassCallGraph ccg = new ClassCallGraph(root);


        Map<String,ClassNode> classMap = buildClassMap(classes);

        for(ClassNode cls : classes){
            cbo.put(cls, new CouplingBetweenObjects(cls,ccg));
            dit.put(cls, new DepthOfInheritanceTree(cls,classMap));
            wmc.put(cls,new WeightedMethodCount(cls));
        }


    }

    private Map<String,ClassNode> buildClassMap(List<ClassNode> classes){
        Map<String,ClassNode> toReturn = new HashMap<>();
        for(ClassNode cls : classes){
            toReturn.put(cls.name,cls);
        }
        return toReturn;
    }

    public String toString(){
        String toReturn = "Class, CBO, DIT, WMC\n";
        for(ClassNode cls : classes){
            toReturn += cls.name+", "
                    + cbo.get(cls).getValue()+", "
                    + dit.get(cls).getValue()+", "
                    + wmc.get(cls).getValue()+"\n";
        }
        return toReturn;
    }

    /**
     * Write out the class diagram to a specified file.
     * @param target
     */
    public void toCSV(File target) throws IOException {
        BufferedWriter fw = new BufferedWriter(new FileWriter(target));
        fw.write(toString());
        fw.flush();
        fw.close();
    }

}
