package hr.fer.seminar.network;

public class InputNetworkLayer implements INetworkLayer {

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

}
