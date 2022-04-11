package fileComparison;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class DuplicateDetectorTest {

    @Test
    public void onThis() throws IOException {
        DuplicateDetector dd = new DuplicateDetector(new File("src/main"),".java", 0);
        dd.fileComparison("thisProject.csv", 0.15);
    }

}