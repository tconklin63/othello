import java.io.PrintStream;
import java.util.Random;

public class NeuralNetwork {
  int numInputs;
  int numHidden;
  int numOutputs;
  double[][] hiddenWeights;
  double[][] outputWeights;
  Random r;

  public NeuralNetwork(int ni, int nh, int no, double initWeightRange) {
    this.numInputs = (ni + 1);
    this.numHidden = (nh + 1);
    this.numOutputs = no;
    this.hiddenWeights = new double[this.numInputs][this.numHidden];
    this.outputWeights = new double[this.numHidden][this.numOutputs];
    this.r = new Random();
    for (int i = 0; i < this.numInputs; i++) {
      for (int j = 0; j < this.numHidden; j++) {
        this.hiddenWeights[i][j] = ((this.r.nextDouble() - 0.5D) * 2.0D * initWeightRange);
      }
    }
    for (int i = 0; i < this.numHidden; i++) {
      for (int j = 0; j < this.numOutputs; j++) {
        this.outputWeights[i][j] = ((this.r.nextDouble() - 0.5D) * 2.0D * initWeightRange);
      }
    }
  }

  public void printWeights() {
    System.out.println("Hidden Weights");
    for (int i = 0; i < this.numInputs; i++) {
      for (int j = 0; j < this.numHidden; j++) {
        System.out.print(this.hiddenWeights[i][j] + " ");
      }
      System.out.println();
    }
    System.out.println("Output Weights");
    for (int i = 0; i < this.numHidden; i++) {
      for (int j = 0; j < this.numOutputs; j++) {
        System.out.print(this.outputWeights[i][j] + " ");
      }
      System.out.println();
    }
  }

  public double[] output(double[] input) throws IllegalArgumentException {
    if (input.length != this.numInputs - 1) {
      throw new IllegalArgumentException("Wrong number of inputs");
    }
    double[] out = { 1.1D, 1.2D, 1.3D };
    return out;
  }
}
