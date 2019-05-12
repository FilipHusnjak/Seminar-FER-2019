package hr.fer.seminar.network;

/**
 * Represents network layer in {@link BackPropagationNetwork}
 * 
 * @author Filip Husnjak
 */
public interface INetworkLayer {

	/**
	 * Returns weights of this layer. Weights are stored in an 
	 * double array. Rows represent neurons while columns
	 * represent weights of the neuron in a specified row.
	 * If network layer is type of {@link InputNetworkLayer}
	 * {@code UnsupportedOperationException is thrown}.
	 * 
	 * @return weights of this layer
	 */
	double[][] getWeights();
	
	/**
	 * Returns outputs of this layer. Size of an returned array is equal
	 * to number of neurons in this layer.
	 * 
	 * @return outputs of this layer
	 */
	double[] getOutputs();
	
	/**
	 * Returns threshold of neurons in this layer.
	 * 
	 * @return threshold of neurons in this layer
	 */
	double[] getThreshold();
	
	/**
	 * Returns number of inputs in this layer.
	 * 
	 * @return number of inputs in this layer
	 */
	int getNumInputs();
	
	/**
	 * Calculates outputs of this layer based on given inputs.
	 * Given input array should have the same size as
	 * 
	 * @param inputs
	 *        inputs connected to this layer
	 */
	void calculateOutputs(double[] inputs);
	
	/**
	 * Resets weights of this layer.
	 */
	void restart();
	
}
