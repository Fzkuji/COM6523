package app;

import structuralAnalysis.ClassDiagram;

import java.io.File;
import java.io.IOException;

public class ClassDiagramGenerator {

    public static void main(String[] args) throws IOException {
        String root = args[0];
        Boolean ignoreLibs = Boolean.parseBoolean(args[1]);
        Boolean ignoreInnerClasses = Boolean.parseBoolean(args[2]);
        String signaturePrefix = args[3];
        String output = args[4];

        ClassDiagram cd = new ClassDiagram(root, ignoreLibs, ignoreInnerClasses, signaturePrefix);
        cd.writeDot(new File(output));
    }
}
