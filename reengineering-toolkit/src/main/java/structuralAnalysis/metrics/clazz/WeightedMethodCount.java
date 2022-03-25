package structuralAnalysis.metrics.clazz;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import structuralAnalysis.metrics.method.CyclomaticComplexityMetric;

import java.util.Map;

public class WeightedMethodCount extends ClassMetric {


    public WeightedMethodCount(ClassNode element) {
        super(element);
    }

    @Override
    protected Double computeMetric() {
        Double total = 0D;
        for(MethodNode method : element.methods){
            CyclomaticComplexityMetric cc = new CyclomaticComplexityMetric(method);
            total = total + cc.getValue();
        }
        return total;
    }
}
