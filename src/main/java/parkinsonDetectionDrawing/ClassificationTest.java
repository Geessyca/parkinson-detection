package parkinsonDetectionDrawing;

import java.io.File;

import static parkinsonDetectionDrawing.ImageProjection.getProjection;

public class ClassificationTest {
    private static void createProjectionTest(){
        getProjection("PATH_TEST_HEALTHY_SPIRAL");
        getProjection("PATH_TEST_PARKINSON_SPIRAL");
    }

    public static void startClassificationTest(){
        File fileTrain = new File(System.getenv("PATH_TEST"));
        if (fileTrain.exists()) {
            fileTrain.delete();
        }
        createProjectionTest();
    }

}
