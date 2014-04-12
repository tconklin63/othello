import java.io.PrintStream;

public class TestNN {
  public static void main(String[] args) {
    NeuralNetwork nn = new NeuralNetwork(3, 5, 1, 0.02D);
    nn.printWeights();
    double[] in = { 1.1D, 1.2D, 1.3D, 1.4D };
    double[] out = { 0.0D };
    try {
      out = nn.output(in);
    } catch (IllegalArgumentException e) {
      System.out.println("Wrong number of inputs to the Neural Network");
    }
    for (int i = 0; i < out.length; i++) {
      System.out.print(out[i] + " ");
    }
    System.out.println();
  }
}