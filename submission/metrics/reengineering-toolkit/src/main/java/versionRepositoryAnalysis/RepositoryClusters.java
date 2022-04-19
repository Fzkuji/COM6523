package versionRepositoryAnalysis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A small app to read CSV files of Git Logs, and to return a visualisation of
 * clusters of files that are edited together.
 */

public class RepositoryClusters {

    Map<String, Set<String>> timeStampsToFiles;
    Pairs pairs = null;

    public RepositoryClusters(String csvFile){
        timeStampsToFiles = new HashMap<>();
        Reader in = null;
        try {
            in = new FileReader(csvFile);

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withEscape('\\').withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                String timeStamp = record.get("Timestamp");
                String file = record.get(" File");
                addToMap(timeStamp,file);
            }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pairs = buildPairs();
    }

    private Pairs buildPairs() {
        Pairs p = new Pairs();
        for(String timeStamp : timeStampsToFiles.keySet()){
            Set<String> files = timeStampsToFiles.get(timeStamp);
            Set<String> done = new HashSet<>();
            for(String file : files){
                done.add(file);
                for(String other : files){
                    if(!done.contains(other))
                        p.addPair(file,other);
                }
            }
        }
        return p;
    }

    private void addToMap(String timeStamp, String file) {
        if(timeStampsToFiles.containsKey(timeStamp)){
            timeStampsToFiles.get(timeStamp).add(file);
        }
        else{
            Set<String> files = new HashSet<>();
            files.add(file);
            timeStampsToFiles.put(timeStamp,files);
        }
    }

    public void writeToDot(String output) throws IOException {
        pairs.writeDot(new File(output));
    }

}
