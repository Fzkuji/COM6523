package dependenceAnalysis.interprocedural;

import dependenceAnalysis.util.Graph;
import dependenceAnalysis.util.Signature;

import java.util.HashSet;
import java.util.Set;

public class ClassCallGraph extends CallGraph{


    Graph<String> classCG = new Graph<String>();

    /**
     * Constructor, taking as input root-directory (String) for project.
     *
     * @param root
     */
    public ClassCallGraph(String root) {
        super(root);
        buildClassCallGraph();
    }

    public Graph<String> getClassCG(){
        return classCG;
    }

    private void buildClassCallGraph() {
        classCG = new Graph<>();
        Set<String> classes = new HashSet<>();

        //Create a set of all of the classes.
        for(Signature sig : callGraph.getNodes()){
            classes.add(sig.getOwner());
        }
        //Add the classes as nodes to the graph.
        for(String owner:classes){
            classCG.addNode(owner);
        }

        for(Signature from : callGraph.getNodes()){
            for(Signature to : callGraph.getSuccessors(from)){
                String f = from.getOwner();
                String t = to.getOwner();

                if(classes.contains(f) && classes.contains(t)){
                    classCG.addEdge(f,t);
                }

            }
        }
    }


    public String toString(){
        return classCG.toString();
    }
}
