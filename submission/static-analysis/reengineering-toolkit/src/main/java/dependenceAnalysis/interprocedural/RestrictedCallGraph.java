package dependenceAnalysis.interprocedural;

import dependenceAnalysis.util.Graph;
import dependenceAnalysis.util.Signature;

public class RestrictedCallGraph extends CallGraph{

    protected String packagePrefix;

    /**
     * Constructor, taking as input root-directory (String) for project.
     *
     * @param root
     */
    public RestrictedCallGraph(String root, String packagePrefix) {
        super(root);
        this.packagePrefix = packagePrefix;
    }


    public String toString(){
        Graph<Signature> printable = new Graph<Signature>();
        for(Signature sig : callGraph.getNodes()){
            if(!sig.getOwner().startsWith(packagePrefix))
                continue;
            if(isConnected(sig))
                printable.addNode(sig);
        }
        for(Signature sig : printable.getNodes()){
            if(!sig.getOwner().startsWith(packagePrefix))
                continue;
            if(!isConnected(sig))
                continue;
            for(Signature to : callGraph.getSuccessors(sig)){
                if(!to.getOwner().startsWith(packagePrefix))
                    continue;
                if(!isConnected(to))
                    continue;
                printable.addEdge(sig,to);
            }
        }
        return printable.toString();
    }
}
