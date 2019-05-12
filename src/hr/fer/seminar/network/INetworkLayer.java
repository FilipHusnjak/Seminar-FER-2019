package hr.fer.seminar.network;

public interface INetworkLayer {

	double[][] getWeights();
	
	double[] getOutputs();
	
	double[] getThreshold();
	
	int getNumInputs();
	
	void calculateOutputs(double[] inputs);
	
	void restart();
	
}
