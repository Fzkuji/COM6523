package structuralAnalysis;

import dependenceAnalysis.interprocedural.ClassCallGraph;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClassCallDiagramTest {

    @Test
    public void classCallDiagramTest(){
        ClassCallDiagram ccd = new ClassCallDiagram("target/classes", false, true, "");
        System.out.println(ccd.toString());
    }

}