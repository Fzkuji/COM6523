package dependenceAnalysis.interprocedural;


import dependenceAnalysis.util.Graph;
import dependenceAnalysis.util.Signature;
import org.objectweb.asm.tree.*;
import util.ASMClassReader;

import java.io.File;
import java.util.*;

/**
 * Created by neilwalkinshaw on 26/07/2018.
 */
public class CallGraph {

    // Map of inheritance tree, from class to set of sub-classes.
    protected Map<ClassNode,Set<ClassNode>> subclasses;

    // Map to link class names to corresponding ClassNode objects
    protected Map<String,ClassNode> classNodes;

    // Graph structure to store call graph. Nodes are method Signature objects
    protected Graph<Signature> callGraph;



    /**
     * Constructor, taking as input root-directory (String) for project.
     * @param root
     */
    public CallGraph(String roots){

        List<ClassNode> classes = new ArrayList<>();

        for(String root : roots.split(",")) {
            if(root.isEmpty()) {
                continue;
            }
            File dir = new File(root);

            classNodes = new HashMap<String,ClassNode>();

            ASMClassReader acr = new ASMClassReader();

            // Traverse root directory structure and obtain all classes.
            List<ClassNode> toBeAdded = acr.processDirectory(dir,"");
            if(toBeAdded != null) {
                classes.addAll(toBeAdded);
            }
        }

        for(ClassNode cn : classes){
            classNodes.put(cn.name,cn);
        }

        subclasses = new HashMap<ClassNode, Set<ClassNode>>();

        //For each class,
        buildInheritanceTrees(classes);

        callGraph = new Graph<Signature>();

        buildGraph();

    }

    protected void buildGraph(){
        for(ClassNode cn : classNodes.values()){
            for(MethodNode mn : cn.methods){
                process(cn,mn);
            }
        }
    }

    /**
     * From the list classes, map each class to the set of sub-classes.
     * This is accomplished by, for every class X, identifying its super-class (ClassNode.superName),
     * obtaining the set of sub-classes for that super-class, and adding X to that set.
     * Should store inheritance tree in subClasses map (from parent class to set of sub-classes).
     *
     * @param classes
     */
    private void buildInheritanceTrees(List<ClassNode> classes) {
        for(ClassNode cn : classes){
            String superClass = cn.superName;
            if(!classNodes.containsKey(superClass))
                addSubClass(classNodes.get(cn.superName),cn);
        }
    }

    private void addSubClass(ClassNode superClass, ClassNode name) {
        if(subclasses.containsKey(superClass)){
            Collection<ClassNode> subClasses = subclasses.get(superClass);
            subClasses.add(name);
        }
        else{
            Set<ClassNode> subClasses = new HashSet<ClassNode>();
            subClasses.add(name);
            subclasses.put(superClass,subClasses);
        }
    }


    protected void process(ClassNode owner,MethodNode current){
        Signature fromSig = new Signature(owner.name,current.name,current.desc);
        InsnList instructions = current.instructions;
        for(int i = 0; i<instructions.size(); i++){
            AbstractInsnNode instruction = instructions.get(i);
            //If the instruction is a call to another method...
            if(instruction.getType() == AbstractInsnNode.METHOD_INSN){
                MethodInsnNode call = (MethodInsnNode) instruction;
                Signature target = new Signature(call.owner,call.name,call.desc);
                String targetClassName = target.getOwner();
                if(!classNodes.containsKey(targetClassName))
                    continue;
                ClassNode targetClass = classNodes.get(targetClassName);
                Collection<MethodNode> targetsForCall = getAllCandidates(target, targetClass);
                for(MethodNode mn : targetsForCall){
                    Signature targetSig = new Signature(targetClassName,mn.name,mn.desc);
                    callGraph.addNode(fromSig);
                    callGraph.addNode(targetSig);
                    callGraph.addEdge(fromSig,targetSig);
                }
            }
        }
    }


    /**
     * for a given target method in a target class, check whether there are other potential
     * targets within the class hierarchy below targetClass, and add these to the call graph
     * (stored in the callGraph class attribute).
     * @param target
     * @param targetClass
     * @return
     */
    private Collection<MethodNode> getAllCandidates(Signature target, ClassNode targetClass) {
        Collection<MethodNode> targets = new HashSet<MethodNode>();
        MethodNode targetMethod = findMethod(targetClass,target);
        if(targetMethod!=null)
            targets.add(targetMethod);
        LinkedList<ClassNode> subs = new LinkedList<>();
        if(subclasses.containsKey(targetClass))
            subs.addAll(subclasses.get(targetClass));
        while(!subs.isEmpty()) {
            ClassNode subClass = subs.poll();
            MethodNode overridingMethod = findMethod(subClass,target);
            if(overridingMethod!=null){
                targets.add(overridingMethod);
            }
            if(subclasses.containsKey(subClass))
                subs.addAll(subclasses.get(subClass));
        }
        return targets;
    }

    private MethodNode findMethod(ClassNode targetClass, Signature target) {
        for(MethodNode mn : targetClass.methods){
            if(mn.name.equals(target.getMethod()) && mn.desc.equals(target.getSignature()))
                return mn;
        }
        return null;
    }


    protected boolean isConnected(Signature sig){
        return (!callGraph.getSuccessors(sig).isEmpty() | !callGraph.getPredecessors(sig).isEmpty());
    }

    /**
     * To create the string for GraphViz we just use the JGraphT toString() method for the call graph.
     * To remove redundant nodes (i.e. methods that are not the target or source of a call), we copy the graph
     * in such a way that such nodes are avoided.
     */
    public String toString(){
        Graph<Signature> printable = new Graph<Signature>();
        for(Signature sig : callGraph.getNodes()){
            if(isConnected(sig))
                printable.addNode(sig);
        }
        for(Signature sig : printable.getNodes()){
            for(Signature to : callGraph.getSuccessors(sig)){
                if(isConnected(to))
                    printable.addEdge(sig,to);
            }
        }
        return printable.toString();
    }


    public Graph<Signature> getCallGraph(){
        return callGraph;
    }

    public int incomingCalls(ClassNode cn, MethodNode mn){
        Signature sig = new Signature(cn.name,mn.name,mn.desc);
        int numIncoming = callGraph.getPredecessors(sig).size();
        return numIncoming;
    }

    public int outgoingCalls(ClassNode cn, MethodNode mn){
        Signature sig = new Signature(cn.name,mn.name,mn.desc);
        int numOutgoing = callGraph.getSuccessors(sig).size();
        return numOutgoing;
    }

    public Collection<ClassNode> getPredecessorsForClass(ClassNode cn){
        Collection<ClassNode> retSet = new HashSet<>();
        for(MethodNode mn : cn.methods){
            Signature sig = new Signature(cn.name,mn.name,mn.desc);
            if(!callGraph.getNodes().contains(sig))
                continue;
            for(Signature pred : callGraph.getPredecessors(sig)){
                ClassNode owner = classNodes.get(pred.getOwner());
                retSet.add(owner);
            }
        }
        return retSet;
    }

    public Collection<ClassNode> getSuccessorsForClass(ClassNode cn){
        Collection<ClassNode> retSet = new HashSet<>();
        for(MethodNode mn : cn.methods){
            Signature sig = new Signature(cn.name,mn.name,mn.desc);
            if(!callGraph.getNodes().contains(sig))
                continue;
            for(Signature succ : callGraph.getSuccessors(sig)){
                ClassNode owner = classNodes.get(succ.getOwner());
                retSet.add(owner);
            }
        }
        return retSet;
    }

}
