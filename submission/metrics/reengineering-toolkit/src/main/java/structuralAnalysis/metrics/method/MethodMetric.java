package structuralAnalysis.metrics.method;

import org.objectweb.asm.tree.MethodNode;
import structuralAnalysis.metrics.Metric;

public abstract class MethodMetric extends Metric<MethodNode> {

    public MethodMetric(MethodNode element) {
        super(element);
    }
}
