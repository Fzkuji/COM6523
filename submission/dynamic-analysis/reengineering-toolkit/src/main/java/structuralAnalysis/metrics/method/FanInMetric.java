package structuralAnalysis.metrics.method;

import dependenceAnalysis.interprocedural.CallGraph;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class FanInMetric extends MethodMetric {

    protected CallGraph cg;
    protected ClassNode cn;

    public FanInMetric(ClassNode owner, MethodNode element, CallGraph cg) {
        super(element);
        this.cg = cg;
        this.cn = owner;
    }

    @Override
    protected Double computeMetric() {
        return (double) cg.incomingCalls(cn, element);
    }
}
