package fileComparison;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * Created by neilwalkinshaw on 21/11/2016.
 */
public class DuplicateDetector {

    List<File> files;
    String suffix;
    int minimumLines;


    public DuplicateDetector(File root, String suffix, int minimumLines) throws FileNotFoundException {
        files = new ArrayList<File>();
        this.suffix = suffix;
        this.minimumLines = minimumLines;
        populateFiles(root);

    }

    private void populateFiles(File root) throws FileNotFoundException {
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(root.listFiles()));
        for(File f: files) {
            if(f.isDirectory()) {
                populateFiles(f);
            }
            else if(f.getName().endsWith(suffix)){
                if(getLineCount(f) > minimumLines)
                    this.files.add(f);

            }
        }
    }

    public void fileComparison(String output, double threshold) throws IOException {
       FileComparator fc = new FileComparator(files);
       double[][] fileSimilarities = fc.compare();
       BufferedWriter fw = new BufferedWriter(new FileWriter(new File(output)));
       fw.write("From, To, Jaccard\n");
       for(int i = 0; i< files.size(); i++){
           for(int j = i+1; j<files.size(); j++){

               if(fileSimilarities[i][j]>=threshold)
                fw.write(files.get(i)+", "+files.get(j)+", "+fileSimilarities[i][j]+"\n");

           }
       }
       fw.flush();
       fw.close();
    }

    public List<File> getFiles(){
        return files;
    }

    //Function is passed a files and returns the number of lines as integer
    public int getLineCount(File file) throws FileNotFoundException {
        Scanner scnr = new Scanner(file);
        int count = 0;
        while (scnr.hasNextLine()){
            count++;
            scnr.nextLine();
        }
        return count;
    }
}
