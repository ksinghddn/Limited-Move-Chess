package nn;

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class Network implements LearningEventListener {
	private MultiLayerPerceptron mlp;
	private MomentumBackpropagation mbp;

	private static final double highInputLimit = 4;
	private static final double lowInputLimit = -4;
	private static final int maxIterations = 20;
	private static final double learningRate = 0.05;
	private static final double momentum = 0.5;

	public Network() {
		mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 16, 10, 1);
		mbp = new MomentumBackpropagation();
		mlp.setLearningRule(mbp);
		mbp.addListener(this);
		mbp.setBatchMode(true);
		mbp.setLearningRate(learningRate);
		mbp.setMomentum(momentum);
		mbp.setMaxIterations(maxIterations);
		mbp.setMinErrorChange(0.000001);
	}

	public double propogate(double[] input) {
		mlp.setInput(normalize(input, lowInputLimit, highInputLimit));
		mlp.calculate();
		return mlp.getOutput()[0];
	}

	private double[] normalize(double[] input, double lowLimit, double highLimit) {
		double[] normalizedInput = new double[input.length];
		Arrays.sort(input);
		double minimum = input[0];
		double maximum = input[input.length - 1];

		for (int i = 0; i < input.length; i++) {
			normalizedInput[i] = ((highLimit - lowLimit) * (input[i] - minimum))
					/ (maximum - minimum) + lowLimit;
		}
		return normalizedInput;
	}

	public void train(DataSet dataSet) {
		System.out.println("Learning from " + dataSet.size() + " rows");
		dataSet.normalize();
		mlp.learn(dataSet);
	}

	public void save(String filePath) {
		mlp.save(filePath);
	}

	@SuppressWarnings({ "deprecation" })
	public void load(String filePath) {
		mlp = (MultiLayerPerceptron) NeuralNetwork.load(filePath);
	}

	public void handleLearningEvent(LearningEvent event) {
		System.out.println("Iteration " + mbp.getCurrentIteration() + " : "
				+ mbp.getTotalNetworkError());
	}
}
