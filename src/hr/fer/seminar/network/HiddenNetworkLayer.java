package hr.fer.seminar.network;

import java.util.Random;

public class HiddenNetworkLayer implements INetworkLayer {

	private final double[][] weights;
	
	private final double[] outputs;
		
	private double[] threshold;
	
	private int numInputs;
	
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
