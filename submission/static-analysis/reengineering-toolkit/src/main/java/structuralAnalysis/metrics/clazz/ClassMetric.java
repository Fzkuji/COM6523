package structuralAnalysis.metrics.clazz;

import org.objectweb.asm.tree.ClassNode;
import structuralAnalysis.metrics.Metric;


public abstract class ClassMetric extends Metric<ClassNode> {

    public ClassMetric(ClassNode element) {
        super(element);
    }
}
