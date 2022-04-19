package app;

import dependenceAnalysis.interprocedural.CallGraph;
import dependenceAnalysis.interprocedural.ClassCallGraph;
import dependenceAnalysis.interprocedural.RestrictedCallGraph;
import dependenceAnalysis.util.Signature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ClassCallGraphGenerator {

    public static void main(String[] args) throws IOException {
        ClassCallGraph cg = null;

        cg = new ClassCallGraph(args[0]);

        getFatInForClass(cg);
        getFatOutForClass(cg);
        System.out.println(cg);
    }

    private static void getFatInForClass(ClassCallGraph cg) throws IOException {
        String toReturn = "Class, Number of uses\n";
        for(String sig : cg.getClassCG().getNodes()){
            int incoming = cg.getClassCG().getPredecessors(sig).size();
            toReturn += sig + ", " + incoming + "\n";
            BufferedWriter fw = new BufferedWriter(new FileWriter("FatInForClass.csv"));
            fw.write(toReturn);
            fw.flush();
            fw.close();
        }
    }

    private static void getFatOutForClass(ClassCallGraph cg) throws IOException {
        String toReturn = "Class, Number of uses\n";
        for(String sig : cg.getClassCG().getNodes()){
            int incoming = cg.getClassCG().getSuccessors(sig).size();
            toReturn += sig + ", " + incoming + "\n";
            BufferedWriter fw = new BufferedWriter(new FileWriter("FatOutForClass.csv"));
            fw.write(toReturn);
            fw.flush();
            fw.close();
        }
    }

}
