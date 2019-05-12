package hr.fer.seminar.network;

import java.util.Random;

/**
 * Represents hidden network layer that has defined adjustable weights.
 * 
 * @author Filip Husnjak
 */
public class HiddenNetworkLayer implements INetworkLayer {

	/**
	 * Weights of this layer
	 */
	private final double[][] weights;
	
	/**
	 * Outputs of this layer
	 */
	private final double[] outputs;
		
	/**
	 * Threshold of neurons in this layer
	 */
	private double[] threshold;
	
	/**
	 * Number of inputs of this layer
	 */
	private int numInputs;
	
	/**
	 * Constructs new {@link HiddenNetworkLayer} with specified neuron count
	 * and number of inputs.
	 * 
	 * @param neuronCount
	 *        number of neurons in this layer
	 * @param numInputs
	 *        number of inputs connected to this layer
	 * @throws IllegalArgumentException if either neuron count or number of inputs are not positive
	 */
	public HiddenNetworkLayer(int neuronCount, int numInputs) {
		if (neuronCount <= 0 || numInputs <= 0) {
			throw new IllegalArgumentException("Number of inputs and neurons has to be posivive number!");
		}
		this.numInputs = numInputs;
		weights = new double[neuronCount][numInputs];
		threshold = new double[neuronCount];
		outputs = new double[neuronCount];
		setWeights();
	}
	
	/**
	 * Initializes weights of this layer.
	 */
	private void setWeights() {
		Random rand = new Random();
		for (int i = 0; i < weights.length; ++i) {
			threshold[i] = rand.nextDouble() - 0.5; // between [-0.5, 0.5]
			for (int j = 0; j < weights[i].length; ++j) {
				weights[i][j] = rand.nextDouble() - 0.5; // between [-0.5, 0.5]
			}
		}
	}
	
	@Override
	public void calculateOutputs(double[] inputs) {
		if (inputs.length != weights[0].length) {
			throw new IllegalArgumentException("Wrong number of inputs provided!");
		}
		for (int i = 0; i < weights.length; ++i) {
			double weightedSum = threshold[i];
			for (int j = 0; j < weights[i].length; ++j) {
				weightedSum += inputs[j] * weights[i][j];
			}
			outputs[i] = applyActivationFunction(weightedSum);
		}
	}

	/**
	 * Calculates and returns the value of sigmoid function.
	 * 
	 * @param weightedSum
	 *        used to calculated value of sigmoid function
	 * @return the value of sigmoid function
	 */
	private double applyActivationFunction(double weightedSum) {
		return 1.0 / (1 + Math.exp(-weightedSum));
	}
	
	@Override
	public double[][] getWeights() {
		return weights;
	}
	
	@Override
	public double[] getOutputs() {
		return outputs;
	}
	
	@Override
	public double[] getThreshold() {
		return threshold;
	}

	@Override
	public void restart() {
		setWeights();
	}

	@Override
	public int getNumInputs() {
		return numInputs;
	}
	
}
