public class Digit {
    double[] data;
    double[] label;

    public Digit(double[] data, int label){
        this.label = new double[10];
        this.data = data;
        this.label[label] = 1;
    }
}
