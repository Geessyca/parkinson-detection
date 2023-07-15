package parkinsonDetectionDrawing;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;

public class MLPClassification {
    public static void classificationForMLP() {
        try {
            // Carregar conjunto de treinamento
            CSVLoader trainLoader = new CSVLoader();
            trainLoader.setSource(new File(System.getenv("PATH_TRAIN")));
            Instances trainData = trainLoader.getDataSet();

            // Definir o índice do atributo classe
            int classIndex = trainData.numAttributes() - 1;
            trainData.setClassIndex(classIndex);

            // Criar classificador MLP (Multilayer Perceptron)
            MultilayerPerceptron mlp = new MultilayerPerceptron();
            mlp.setHiddenLayers("50");
            //mlp.setHiddenLayers("100");
            //mlp.setHiddenLayers("200");
            mlp.buildClassifier(trainData);
            System.out.println(mlp.getHiddenLayers());

            // Carregar conjunto de teste
            CSVLoader testLoader = new CSVLoader();
            testLoader.setSource(new File(System.getenv("PATH_TEST")));
            Instances testData = testLoader.getDataSet();

            // Definir o índice do atributo classe
            testData.setClassIndex(classIndex);

            // Classificar as instâncias de teste
            Evaluation eval = new Evaluation(trainData);
            eval.evaluateModel(mlp, testData);

            // Imprimir resumo da avaliação
            System.out.println(eval.toSummaryString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
