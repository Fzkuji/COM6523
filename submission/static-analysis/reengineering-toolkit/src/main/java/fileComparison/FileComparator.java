package fileComparison;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by neilwalkinshaw on 21/11/2016.
 */
public class FileComparator {

    List<File> files;
    Set<String> unwantedLines = new HashSet<>();

    public FileComparator(List<File> files){
        this.files = files;
        unwantedLines.add("}");
        unwantedLines.add("");
        unwantedLines.add("{");
        unwantedLines.add(";");
        unwantedLines.add("*");
        unwantedLines.add("/**");
        unwantedLines.add("*/");
    }

    /**
     * Turn a string (representing a line of code) into a list of individual characters.
     * This is a utility method to enable the calculation of the Jaccard Similarity between two lines of code
     * (where the sets are obtained by turning the strings into sets of characters).
     * @param str
     * @return
     */
    public static List<Character> stringtoList(String str)
    {
        return str.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
    }


    /**
     * This returns a 2D array, where the indices correspond to the indices in the list of files, and
     * the value corresponds to the score, computed by the jaccard function.
     *
     * @return
     */
    public double[][] compare(){

        double[][] scores = new double[files.size()][files.size()];
        for(int i = 0; i<files.size(); i++){
            for(int j = i; j<files.size(); j++){
                Set<String> fileA = new HashSet<>();
                fileA.addAll(getStrings(files.get(i)));
                Set<String> fileB = new HashSet<>();
                fileB.addAll(getStrings(files.get(j)));
                scores[i][j] = jaccard(fileA,fileB);
                scores[j][i] = scores[i][j];
            }
        }
        return scores;
    }

    /**
     * Computes the jaccard similarity between a and b.
     * @param a
     * @param b
     * @return
     */
    protected double jaccard(Set a, Set b){
        HashSet intersection = new HashSet();
        intersection.addAll(a);
        intersection.retainAll(b);
        double score = (double)intersection.size() / ((double)a.size() + (double)b.size() - (double)intersection.size());
        return score;
    }

    /**
     * Returns a map linking each file to a list of strings, where each string corresponds to the contents of the file.
     * The list is processed to remove unwanted lines.
     * @return
     */
    public Map<File,List<String>> extractStrings() {
        Map<File,List<String>> filesToStrings = new HashMap<>();
        for(File f : files) {
            List<String> stringList = new ArrayList<>();
            List<String> forFile = getStrings(f);
            stringList.addAll(forFile);
            stringList.removeAll(unwantedLines);
            filesToStrings.put(f,stringList);
        }
        return filesToStrings;
    }

    /**
     * Returns the list of strings for a given file. Trims each string to remove whitespace.
     * @param f
     * @return
     */
    private List<String> getStrings(File f) {
        List<String> forFile = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line != null) {
                line = line.trim();
                if(line.startsWith("*") || line.startsWith("/")) {
                    line = br.readLine();
                    continue;
                }
                forFile.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forFile;
    }

    /**
     * For the list of files, create a boolean matrix that compares each line with each other line. Whenever
     * a pair of lines is identical, the matrix contains a TRUE entry for that pair, otherwise it contains a FALSE.
     * @param extracted
     * @return
     */
    public boolean[][] detailedBooleanCompare(Map<File,List<String>> extracted){
        int size = 0;
        for(List<String> lines : extracted.values()){
            size+=lines.size();
        }
        boolean[][] matrix = new boolean[size][size];
        int dim1Index = 0;

        for(File fromFile : extracted.keySet()){
            List<String> fileStrings = extracted.get(fromFile);
            for(int i = 0; i<fileStrings.size(); i++){
                dim1Index = dim1Index+i;
                String from = fileStrings.get(i);
                int dim2Index = 0;
                for(File toFile : extracted.keySet()) {
                    List<String> contents = extracted.get(toFile);
                    for (int j = 0; j < contents.size(); j++) {
                        dim2Index = dim2Index + j;
                        String to = contents.get(j);
                        boolean match = from.equals(to);
                        matrix[dim1Index][dim2Index] = match;
                    }
                }
            }
        }
        return matrix;
    }

    /**
     * Instead of comparing individual strings for equality, measure Jaccard distance between them.
     * Use the threshold as a cutoff (threshold of 1 is equivalent to checking for equality).
     * @param threshold
     * @return
     */
    public boolean[][] detailedBooleanCompareWithJaccard(Map<File,List<String>> extracted, double threshold){
        int size = 0;
        for(List<String> lines : extracted.values()){
            size+=lines.size();
        }
        boolean[][] matrix = new boolean[size][size];
        int dim1Index = 0;

        for(File fromFile : extracted.keySet()){
            List<String> fileStrings = extracted.get(fromFile);
            for(int i = 0; i<fileStrings.size(); i++){

                String from = fileStrings.get(i);
                int dim2Index = 0;
                for(File toFile : extracted.keySet()) {
                    List<String> contents = extracted.get(toFile);

                    for (int j = 0; j < contents.size(); j++) {
                        String to = contents.get(j);
                        Set fromString = new HashSet();
                        Set toString = new HashSet();
                        fromString.addAll(stringtoList(from));
                        toString.addAll(stringtoList(to));
                        double score = jaccard(fromString,toString);
                        boolean match = (score>=threshold);
                        matrix[dim1Index][dim2Index]=match;
                        dim2Index = dim2Index + 1;
                    }


                }
                dim1Index = dim1Index+1;
            }
        }
        return matrix;
    }



}
