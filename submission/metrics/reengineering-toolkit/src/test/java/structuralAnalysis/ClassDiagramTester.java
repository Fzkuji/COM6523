package structuralAnalysis;

import org.junit.Test;

public class ClassDiagramTester {

    @Test
    public void analysisCodeClassDiagramTest(){
        ClassDiagram cd = new ClassDiagram("target/classes", false, true, "");
        System.out.println(cd.toString());
    }

}
