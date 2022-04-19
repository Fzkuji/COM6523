package app;

import fileComparison.FileComparator;
import fileComparison.visualise.BitmapVisualiser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalFileComparison {

    /**
     * args[0] - File A
     * args[1] - File B
     * args[2] - Value between 0 and 1. Jaccard index threshold - anything larger than this score is considered a match.
     * args[3] - Output png file.
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        ArrayList<File> files = new ArrayList<>();
        File a = new File(args[0]);
        File b = new File(args[1]);
        files.add(a);
        files.add(b);
        FileComparator fc = new FileComparator(files);
        Map<File, List<String>> extractedStrings = fc.extractStrings();
        boolean[][] comparison = fc.detailedBooleanCompareWithJaccard(extractedStrings,Double.parseDouble(args[2]));
        BitmapVisualiser bmv = new BitmapVisualiser();
        bmv.produceVisualisation(comparison,extractedStrings,new File(args[3]));
    }
}
