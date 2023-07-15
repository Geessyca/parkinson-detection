package parkinsonDetectionDrawing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageProjection extends JFrame {

    private static boolean isTrain=false;

    private static int[] getPixelData(BufferedImage img, int x, int y) {
        int argb = img.getRGB(x, y);

        int rgb[] = new int[]{
                (argb >> 16) & 0xff, //red
                (argb >> 8) & 0xff, //green
                (argb) & 0xff //blue
        };

        return rgb;
    }

    public static void getImagesProjection(int projectionSize, String path, String fileName, String classificationName) {
        BufferedImage img;
        int[][] rmat = null;
        int[][] gmat = null;
        int[][] bmat = null;
        File directory = new File(path);
        File[] imageFiles = directory.listFiles((dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".png") || lowercaseName.endsWith(".jpg") || lowercaseName.endsWith(".jpeg");
        });

        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                try {
                    img = ImageIO.read(imageFile);
                    rmat = new int[img.getHeight()][img.getWidth()];
                    gmat = new int[img.getHeight()][img.getWidth()];
                    bmat = new int[img.getHeight()][img.getWidth()];
                    for (int i = 0; i < img.getHeight(); i++) {
                        for (int j = 0; j < img.getWidth(); j++) {
                            rmat[i][j] = getPixelData(img, j, i)[0];
                            gmat[i][j] = getPixelData(img, j, i)[1];
                            bmat[i][j] = getPixelData(img, j, i)[2];
                        }
                    }
                    int[][] binaryMat = new int[bmat.length][bmat[0].length];
                    for(int l =0; l < bmat.length; l++){
                        for(int c =0; c < bmat[0].length; c++){
                            int mean = (bmat[l][c] + rmat[l][c] + gmat[l][c])/3;
                            binaryMat[l][c] = mean > 167 ? 0 : 1;
                        }
                    }
                    creatFileWithProjection(fileName,classificationName,projectionSize,binaryMat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void creatFileWithProjection(String fileName, String classificationName, int projectionSize, int[][] binaryMat){
        int[] projectionAux =  new int[binaryMat[0].length];
        for(int c =0; c < binaryMat[0].length; c++){
            for(int l =0; l < binaryMat.length; l++){
                projectionAux[c] += binaryMat[l][c];
            }
        }
        int[] projection =  new int[projectionSize];
        int move = projectionAux.length/projectionSize;
        int offset = 0;
        for(int c = 0; c < projection.length; c++){
            for(int c2 = 0; c2 < move; c2++){
                projection[c] += projectionAux[offset + c2];
            }
            projection[c] = projection[c]/move;
            offset += move;
        }
        try {
            FileWriter writer = new FileWriter(fileName, true);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < projection.length; i++) {
                sb.append(projection[i]);
                if (i < projection.length - 1) {
                    sb.append(",");
                }
            }
            sb.append(",");
            sb.append(classificationName);
            sb.append("\n");
            writer.append(sb.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void getProjection(String pathName){
        String path = System.getenv(pathName).replaceAll("\"","");
        String[] pathParts = path.split("\\\\");
        int projectionSize = 15;
        String classificationName = pathParts[10];
        String fileName = "";
        if (path.contains("training")) {
            fileName = System.getenv("PATH_TRAIN");
            isTrain=true;
        }
        else{
            fileName = System.getenv("PATH_TEST");
        }
        File fileTrain = new File(fileName);
        if (!fileTrain.exists()) {
            try {
                FileWriter writer = new FileWriter(fileName);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < projectionSize; i++) {
                    sb.append("pn"+i);
                    if (i < projectionSize - 1) {
                        sb.append(",");
                    }
                }
                sb.append(",");
                sb.append("classification");
                sb.append("\n");
                writer.append(sb.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getImagesProjection(projectionSize,path, fileName, classificationName);
    }

}
