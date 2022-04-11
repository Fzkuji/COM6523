package fileComparison.visualise;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BitmapVisualiser extends SimilarityVisualiser {

    @Override
    public void produceVisualisation(boolean[][] scores, Map<File,List<String>> files, File target) throws IOException {

        int dimensions = 0;
        for(File f : files.keySet()){
            dimensions+=files.get(f).size();
        }
        dimensions += files.size(); //want to add lines to separate files

        BufferedImage image = new BufferedImage(dimensions, dimensions, BufferedImage.TYPE_INT_RGB);

        int dim1Index = 0;
        int dim1Files = 0;
        for(File fromFile : files.keySet()) {
            List<String> fileStrings = files.get(fromFile);
            for (int i = 0; i < fileStrings.size(); i++) {
                //Going through DIM 1
                int dim2Index = 0;
                int dim2Files = 0;
                for (File toFile : files.keySet()) {
                    //Going through Dim 2
                    List<String> contents = files.get(toFile);
                    for (int j = 0; j < contents.size(); j++) {

                        if(scores[dim1Index-dim1Files][dim2Index-dim2Files]){
                            image.setRGB(dim1Index,dim2Index, Color.yellow.getRGB());
                        }
                        else
                            image.setRGB(dim1Index,dim2Index,Color.GRAY.getRGB());
                        dim2Index++;
                    }
                    dim2Files++;
                    image.setRGB(dim1Index,dim2Index,Color.WHITE.getRGB());
                    dim2Index++;
                }
                dim1Index++;
            }
            dim1Files++;

            for(int i = 0; i<dimensions; i++){
                image.setRGB(dim1Index,i, Color.WHITE.getRGB());
            }
            dim1Index++;
        }
        for(File f : files.keySet()){
            System.out.print(f.getName()+", ");
        }
        ImageIO.write(image,"png",target);
    }
}
