package hr.fer.seminar.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackPropagationNetwork {
	
	private List<INetworkLayer> layers = new ArrayList<>();
	
	private double result;
	
	private double error;
	
	private static double LEARNING_RATE = 0.5;

	public BackPropagationNetwork(int[] network) {
		setup(network);
	}
	
	private void setup(int[] network) {
		layers.add(new InputNetworkLayer());
		for (int i = 1; i < network.length; ++i) {
			layers.add(new HiddenNetworkLayer(network[i], network[i - 1]));
		}
	}
	
	public double calcResult(double x, double y) {
		double[] inputs = new double[] {x, y};
		for (INetworkLayer layer: layers) {
			layer.calculateOutputs(inputs);
			inputs = layer.getOutputs();
		}
		return result = layers.get(layers.size() - 1).getOutputs()[0];
	}
	
	public void propagateError(double desired) {
		double[] errors = new double[] {error = desired - result};
		for (int i = layers.size() - 1; i >= 1; --i) {
			double[] totalErrors = new double[layers.get(i).getNumInputs()];
			Arrays.fill(totalErrors, 0);
			for (int j = 0; j < layers.get(i).getWeights().length; ++j) {
				double output = layers.get(i).getOutputs()[j];
				double delta = derivative(output) * errors[j];
				layers.get(i).getThreshold()[j] += delta * LEARNING_RATE;
				for (int k = 0; k < layers.get(i).getWeights()[j].length; ++k) {
					totalErrors[k] += layers.get(i).getWeights()[j][k] * delta;
					double input = layers.get(i - 1).getOutputs()[k];
					layers.get(i).getWeights()[j][k] += delta * input * LEARNING_RATE;
				}
			}
			errors = totalErrors;
		}
	}
	
	public void restart() {
		for (INetworkLayer layer: layers) {
			layer.restart();
		}
	}
	
	private double derivative(double value) {
		return value * (1 - value);
	}
	
	public double getError() {
		return error;
	}
	
}
