import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MnistDataReader {

    public ArrayList<Digit> readData(String dataFilePath, String labelFilePath) throws IOException {
        ArrayList<Digit> digits = new ArrayList<>();

        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFilePath)));
        int magicNumber = dataInputStream.readInt();
        int numberOfItems = dataInputStream.readInt();
        int nRows = dataInputStream.readInt();
        int nCols = dataInputStream.readInt();

        System.out.println("magic number is " + magicNumber);
        System.out.println("number of items is " + numberOfItems);
        System.out.println("number of rows is: " + nRows);
        System.out.println("number of cols is: " + nCols);

        DataInputStream labelInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(labelFilePath)));
        int labelMagicNumber = labelInputStream.readInt();
        int numberOfLabels = labelInputStream.readInt();

        System.out.println("labels magic number is: " + labelMagicNumber);
        System.out.println("number of labels is: " + numberOfLabels);

        assert numberOfItems == numberOfLabels;

        //Reading the images
        for(int i = 0; i < numberOfItems; i++) {
            double[] data = new double[784];
            for(int j=0; j<784 ;j++){
                data[j] = (double) dataInputStream.readUnsignedByte() / 255;
            }
            int label = labelInputStream.readUnsignedByte();
            digits.add(new Digit(data, label));
        }

        dataInputStream.close();
        labelInputStream.close();
        return digits;
    }
}
