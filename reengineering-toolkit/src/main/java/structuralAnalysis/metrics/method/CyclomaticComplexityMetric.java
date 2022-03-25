package structuralAnalysis.metrics.method;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CyclomaticComplexityMetric extends MethodMetric {


    public CyclomaticComplexityMetric(MethodNode element) {
        super(element);
    }

    @Override
    public Double computeMetric() {
        double branches = 0;
        for(AbstractInsnNode instruction : element.instructions){
            if (instruction.getType() == AbstractInsnNode.JUMP_INSN)
                branches++;
        }
        return branches + 1;
    }
}
