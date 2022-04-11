package app;

import fileComparison.DuplicateDetector;

import java.io.File;
import java.io.IOException;

public class GlobalFileComparison {


    /**
     * args[0] - root directory containing source files
     * args[1] - suffix (e.g. "java")
     * args[2] - name of output csv file
     * args[3] - threshold for jaccard index (file pairs with lower scores will not be included in csv).
     * args[4] - minimum number of lines in the files to be compared
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        DuplicateDetector dd = new DuplicateDetector(new File(args[0]),args[1], Integer.parseInt(args[4]));
        dd.fileComparison(args[2], Double.parseDouble(args[3]));
    }

}
