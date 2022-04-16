package versionRepositoryAnalysis;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Pairs {

    protected Table<String, String, Double> table = TreeBasedTable.create();
    protected int threshold = 3;

    public void addPair(String a, String b){
        if(table.contains(a,b)){
            Double current = table.get(a,b );
            table.put(a,b,current+1);
        }
        else{
            table.put(a,b,1D);
        }
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String toString(){
        StringBuffer dotGraph = new StringBuffer();
        dotGraph.append("digraph classDiagram{\n" +
                "overlap=false;\n" );
        Map<String, Map<String,Double>> rowMap = table.rowMap();
        for(String key : rowMap.keySet()){
            Map<String,Double> secondMap = rowMap.get(key);
            for(String second : secondMap.keySet()){
                Double weight = secondMap.get(second);
                if(weight>=threshold) {
                    dotGraph.append("\"" + key + "\" -> " + "\"" + second + "\"[penwidth = " + (weight*2) + "];\n");
                }
            }
        }
        dotGraph.append("}");
        return dotGraph.toString();
    }

    /**
     * Write out the class diagram to a specified file.
     * @param target
     */
    public void writeDot(File target) throws IOException {
        BufferedWriter fw = new BufferedWriter(new FileWriter(target));
        fw.write(toString());
        fw.flush();
        fw.close();
    }


}
