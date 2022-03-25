package dependenceAnalysis.interprocedural;

import org.junit.Test;

public class CallGraphTester {

    @Test
    public void callGraphTester(){
        CallGraph cg = new CallGraph("target/classes");
        System.out.println(cg.getCallGraph());
    }

    @Test
    public void classCallGraphTester(){
        ClassCallGraph cg = new ClassCallGraph("target/classes");
        System.out.println(cg);
    }

}
