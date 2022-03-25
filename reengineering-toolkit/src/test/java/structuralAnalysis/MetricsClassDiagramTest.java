package structuralAnalysis;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class MetricsClassDiagramTest {

    @Test
    public void testMetricClassDiagramTest() throws IOException {
        MetricsClassDiagram cd = new MetricsClassDiagram("target/classes", true, true, "");
        System.out.println(cd.toString());
    }

 /*
    @Test
    public void testMetricClassDiagramTest_Weka() throws IOException {
        MetricsClassDiagram cd = new MetricsClassDiagram("/Users/neil/Google Drive/Teaching/Sheffield/Reengineering/2019-20/Examples/weka-3.8/weka/build/classes", true, true, "weka.classifiers");
        cd.writeDot(new File("/tmp/example.dot"));
    }
*/
}