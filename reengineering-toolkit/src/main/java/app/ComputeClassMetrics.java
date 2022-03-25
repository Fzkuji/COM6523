package app;

import structuralAnalysis.metrics.ClassMetricAggregator;

import java.io.File;
import java.io.IOException;

public class ComputeClassMetrics {


    /**
     * args[0] - root directory containing classes and packages
     * args[1] - name of output CSV file
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ClassMetricAggregator cma = new ClassMetricAggregator(args[0]);
        File target = new File(args[1]);
        cma.toCSV(target);
    }

}
