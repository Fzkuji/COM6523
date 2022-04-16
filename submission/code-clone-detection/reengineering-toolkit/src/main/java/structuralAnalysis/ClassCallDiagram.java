package structuralAnalysis;

import dependenceAnalysis.interprocedural.ClassCallGraph;

import java.util.Set;

public class ClassCallDiagram extends ClassDiagram {

    ClassCallGraph cg;


    /**
     * Instantiating the class will populate the inheritance and association relations.
     *
     * @param root
     * @param ignoreLibs
     * @param ignoreInnerClasses
     * @param signaturePrefix
     */
    public ClassCallDiagram(String root, boolean ignoreLibs, boolean ignoreInnerClasses, String signaturePrefix) {
        super(root, ignoreLibs, ignoreInnerClasses, signaturePrefix);
        cg = new ClassCallGraph(root);
    }

    //Just copied this toString method here to adapt it ... sure it'll be fine. Nobody will notice.
    public String toString(){
        StringBuffer dotGraph = new StringBuffer();
        dotGraph.append("digraph classDiagram{\n" +
                "graph [splines=ortho, rankdir=BT]\n\n");

        for(String className : includedClasses){
            dotGraph.append("\""+className + "\"[shape = box];\n");
        }

        //Add inheritance relations
        for(String childClass : inheritance.keySet()){
            if(includedClasses.contains(childClass) && includedClasses.contains(inheritance.get(childClass))) {
                String from = "\"" + childClass + "\"";
                String to = "\"" + inheritance.get(childClass) + "\"";
                dotGraph.append(from + " -> " + to + "[arrowhead = onormal];\n");
            }
        }

        //Add associations
        for(String cls : associations.keySet()){
            if(!includedClasses.contains(cls))
                continue;
            Set<String> fields = associations.get(cls);
            for(String field : fields) {
                if(!includedClasses.contains(field))
                    continue;
                String from = "\""+cls +"\"";
                String to = "\""+field+"\"";
                dotGraph.append(from + " -> " +to + "[arrowhead = diamond];\n");
            }
        }

        //Add calls
        for(String node : cg.getClassCG().getNodes()){
            String correctFormatFrom = node.replaceAll("/",".");
            for(String to : cg.getClassCG().getSuccessors(node)){
                String correctFormatTo = to.replaceAll("/",".");
                dotGraph.append("\""+correctFormatFrom + "\" -> \"" +correctFormatTo + "\" [style=dashed];\n");
            }
        }

        dotGraph.append("}");
        return dotGraph.toString();
    }
}
