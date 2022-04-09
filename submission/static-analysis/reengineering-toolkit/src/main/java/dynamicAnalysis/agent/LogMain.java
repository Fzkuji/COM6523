package dynamicAnalysis.agent;

import java.lang.instrument.Instrumentation;

public class LogMain {
    public static void premain(String agentArguments, Instrumentation instrumentation) {
        instrumentation.addTransformer(new Transformer(agentArguments));
    }
}
