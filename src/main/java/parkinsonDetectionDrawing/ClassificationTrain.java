package parkinsonDetectionDrawing;

import java.io.File;

import static parkinsonDetectionDrawing.ImageProjection.getProjection;

public class ClassificationTrain {
    private static void createProjectionTrain(){
        getProjection("PATH_TRAIN_HEALTHY_SPIRAL");
        getProjection("PATH_TRAIN_PARKINSON_SPIRAL");
    }
    public static void startClassificationTrain(){
        createProjectionTrain();
    }
}
