package structuralAnalysis.metrics.clazz;

import dependenceAnalysis.interprocedural.ClassCallGraph;
import dependenceAnalysis.util.Graph;
import org.objectweb.asm.tree.ClassNode;

/**
 * This is an estimate of the CBO, where we only consider method calls. For the proper CBO we would also need to consider
 * data accesses to other classes.
 */

public class CouplingBetweenObjects extends ClassMetric {

    protected ClassCallGraph ccg;

    public CouplingBetweenObjects(ClassNode element, ClassCallGraph classCallGraph) {
        super(element);
        this.ccg = classCallGraph;
    }

    @Override
    protected Double computeMetric() {
        Graph<String> classCallGraph = ccg.getClassCG();
        Double successors = 0D;
        if(classCallGraph.getNodes().contains(element.name))
            successors = (double) classCallGraph.getSuccessors(element.name).size();
        return successors;
    }
}
