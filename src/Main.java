import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Digit> digits = null;
        ArrayList<Digit> testDigits = null;
        Path currentDir = Paths.get(".");
        String dataFilePath = currentDir.toAbsolutePath()+"/train-images.idx3-ubyte";
        String labelFilePath = currentDir.toAbsolutePath()+"/train-labels.idx1-ubyte";

        String evaluateDataFilePath = currentDir.toAbsolutePath()+"/t10k-images-idx3-ubyte";
        String evaluateLabelFilePath = currentDir.toAbsolutePath()+"/t10k-labels-idx1-ubyte";

        try {
            MnistDataReader mnistDataReader = new MnistDataReader();
            digits = mnistDataReader.readData(dataFilePath, labelFilePath);
            testDigits = mnistDataReader.readData(evaluateDataFilePath, evaluateLabelFilePath);
        }catch(IOException e){
            e.printStackTrace();
        }

        NeuralNetwork neuralNetwork = new NeuralNetwork(784, 30, 10);

        for(int i=0; i<3 ;i++) {
            for (Digit digit : digits) {
                neuralNetwork.train(digit.data, digit.label);
            }
        }

        double correct = 0;
        for(Digit digit : testDigits){
            double[] predictedData = neuralNetwork.feedforward(digit.data);
            int index = 0;
            for(int i=1; i<predictedData.length ;i++){
                if(predictedData[i] > predictedData[index]) index = i;
            }
            if(digit.label[index] == 1) correct++;
        }
        System.out.println("Correct answers: "+(correct/10000)*100 + "%");

        new GUI(neuralNetwork);
    }
}
