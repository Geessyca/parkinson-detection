package parkinsonDetectionDrawing;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.NormalizedPolyKernel;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.classifiers.functions.supportVector.RBFKernel;

import java.io.File;
public class SVMClassification {
    public static void classificationForSVM() {
        try {
            // Carregar conjunto de treinamento
            CSVLoader trainLoader = new CSVLoader();
            trainLoader.setSource(new File(System.getenv("PATH_TRAIN")));
            Instances trainData = trainLoader.getDataSet();

            // Definir o índice do atributo classe
            int classIndex = trainData.numAttributes() - 1;
            trainData.setClassIndex(classIndex);

            // Criar classificador SVM com o algoritmo SMO
            SMO svm = new SMO();
            //svm.setKernel(new RBFKernel());
            svm.setKernel(new NormalizedPolyKernel());
            System.out.println(svm.getKernel());
            svm.buildClassifier(trainData);

            // Carregar conjunto de teste
            CSVLoader testLoader = new CSVLoader();
            testLoader.setSource(new File(System.getenv("PATH_TEST")));
            Instances testData = testLoader.getDataSet();

            // Definir o índice do atributo classe
            testData.setClassIndex(classIndex);

            // Classificar as instâncias de teste
            Evaluation eval = new Evaluation(trainData);
            eval.evaluateModel(svm, testData);

            // Imprimir resumo da avaliação
            System.out.println(eval.toSummaryString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
