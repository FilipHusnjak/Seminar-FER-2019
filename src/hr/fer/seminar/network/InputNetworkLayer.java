package hr.fer.seminar.network;

/**
 * First layer in the network. It does not have weighted inputs so methods
 * {@link #getWeights()}, {@link #getOutputs()} and {@link #getThreshold()}
 * are not supported.
 * 
 * @author Filip Husnjak
 */
public class InputNetworkLayer implements INetworkLayer {

	/**
	 * Outputs of this layer
	 */
	private double[] outputs = new double[2];
	
	@Override
	public double[][] getWeights() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double[] getOutputs() {
		return outputs;
	}

	@Override
	public double[] getThreshold() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void calculateOutputs(double[] inputs) {
		if (inputs.length != 2) {
			throw new IllegalArgumentException();
		}
		outputs = inputs;
	}

	@Override
	public void restart() {}

	@Override
	public int getNumInputs() {
		return 2;
	}

}
