package hr.fer.seminar.network;

public interface INetworkLayer {

	public double[][] getWeights();
	
	public double[] getOutputs();
	
	public double[] getThreshold();
	
	public void calculateOutputs(double[] inputs);
	
	public void restart();
	
}
