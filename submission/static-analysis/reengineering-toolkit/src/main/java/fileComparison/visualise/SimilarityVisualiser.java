package fileComparison.visualise;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A small utility class to visualise the similarity between two files, provided as a boolean matrix.
 */

public abstract class SimilarityVisualiser {

    public abstract void produceVisualisation(boolean[][] scores, Map<File,List<String>> strings,
                                              File target)throws IOException;

}
