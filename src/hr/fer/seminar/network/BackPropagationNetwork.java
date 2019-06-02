package hr.fer.seminar.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Back propagation network that creates and stores layers of type {@link #INetworkLayer}.
 * Network is defined and given though constructor as integer array.
 * Length of the array represents how many layers are in the network and number
 * on position {@code i} represents how many neurons are in the layer {@code i}.
 * This class also contains 2 important methods for learning:
 * <ul>
 * <li> {@link #calcResult(double, double)} - Takes 2 arguments that represent inputs and 
 * 		calculates outputs of all neurons in the network from left to right, 
 * 		result of last neuron is stored in {@link #result} variable
 * <li> {@link #propagateError(double)} - Takes 1 argument that represent desired output
 *      of the last output calculation, this method should be called after
 *      {@link #calcResult(double, double)}
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class BackPropagationNetwork {
	
	/**
	 * List of layers in this network
	 */
	private List<INetworkLayer> layers = new ArrayList<>();
	
	/**
	 * Result of the last calculation of {@link #calcResult(double, double)} method
	 */
	private double result;
	
	/**
	 * Error of the last training iteration
	 */
	private double error;
	
	/**
	 * Learning rate of this network, should be less than {@code 1}
	 */
	public static double LEARNING_RATE = 0.5;

	/**
	 * Constructs new {@link BackPropagationNetwork} with given network definition.
	 * Length of the array represents how many layers are in the network and number
	 * on position {@code i} represents how many neurons are in the layer {@code i}.
	 * 
	 * @param network
	 *        network definition
	 * @throws NullPointerException if the given array is {@code null}
	 * @throws IllegalArgumentException if the network is invalid
	 */
	public BackPropagationNetwork(int[] network) {
		Objects.requireNonNull(network, "Given network cannot be null!");
		if (network.length < 2) {
			throw new IllegalArgumentException("Given network has to have at least 2 layers!");
		}
		if (network[0] != 2 || network[network.length - 1] != 1) {
			throw new IllegalArgumentException("Given network has to have 2 inputs and 1 output!");
		}
		setup(network);
	}
	
	/**
	 * Creates the network with random weights.
	 * 
	 * @param network
	 *        network to be created
	 */
	private void setup(int[] network) {
		layers.add(new InputNetworkLayer());
		for (int i = 1; i < network.length; ++i) {
			layers.add(new HiddenNetworkLayer(network[i], network[i - 1]));
		}
	}
	
	/**
	 * Calculates and returns output of the network based on the given inputs.
	 * 
	 * @param x
	 *        first input
	 * @param y
	 *        second input
	 * @return output of the network based on given inputs
	 */
	public double calcResult(double x, double y) {
		double[] inputs = new double[] {x, y};
		for (INetworkLayer layer: layers) {
			layer.calculateOutputs(inputs);
			inputs = layer.getOutputs();
		}
		return result = layers.get(layers.size() - 1).getOutputs()[0];
	}
	
	/**
	 * Calculates and propagates an error of the last output calculation
	 * from right to left. Weights are adjusted accordingly.
	 * 
	 * @param desired
	 *        desired output of the last input combination
	 */
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
	
	/**
	 * Resets all weights in this network.
	 */
	public void restart() {
		for (INetworkLayer layer: layers) {
			layer.restart();
		}
	}
	
	/**
	 * Returns derivative of sigmoid function at the point
	 * with given value.
	 * 
	 * @param value
	 *        value used to calculate derivative
	 * @return derivative of sigmoid function at the point
	 *         with given value
	 */
	private double derivative(double value) {
		return value * (1 - value);
	}
	
	/**
	 * Returns error of the last calculation.
	 * 
	 * @return error of the last calculation
	 */
	public double getError() {
		return error;
	}
	
}
