package fileComparison;

import fileComparison.visualise.BitmapVisualiser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FileComparatorTest {

    @Test
    public void jaccardTest() throws IOException {
        FileComparator fc = new FileComparator(new ArrayList<>());
        HashSet test1 = new HashSet<>();
        test1.addAll(FileComparator.stringtoList("one two four"));
        HashSet test2 = new HashSet<>();
        test2.addAll(FileComparator.stringtoList("one two"));
        System.out.println(fc.jaccard(test1, test2));
    }

    @Test
    public void comparisonTest() throws IOException{
        ArrayList<File> files = new ArrayList<>();
        File a = new File("src/main/java/structuralAnalysis/MetricsClassDiagram.java");
        File b = new File("src/main/java/structuralAnalysis/ClassDiagram.java");
        files.add(a);
        files.add(b);
        FileComparator fc = new FileComparator(files);
        Map<File,List<String>> extractedStrings = fc.extractStrings();
        boolean[][] comparison = fc.detailedBooleanCompareWithJaccard(extractedStrings,0.65);
        BitmapVisualiser bmv = new BitmapVisualiser();
        bmv.produceVisualisation(comparison,extractedStrings,new File("test.png"));
    }

}