package structuralAnalysis.metrics.clazz;

import org.objectweb.asm.tree.ClassNode;

import java.util.Map;

public class DepthOfInheritanceTree extends ClassMetric {

    Map<String,ClassNode> otherClasses;

    public DepthOfInheritanceTree(ClassNode element, Map<String, ClassNode> otherClasses) {
        super(element);
        this.otherClasses = otherClasses;
    }

    @Override
    protected Double computeMetric() {

        Double depth = 0D;
        boolean checkForParent = true;
        ClassNode currentNodeInTree = element;
        while(checkForParent){
            String parentClass = currentNodeInTree.superName;
            if(otherClasses.keySet().contains(parentClass)){

                currentNodeInTree = otherClasses.get(parentClass);
            }
            else{
                checkForParent = false;
            }
            depth++;
        }
        return depth;
    }
}
