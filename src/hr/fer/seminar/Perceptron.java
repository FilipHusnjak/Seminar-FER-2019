package hr.fer.seminar;

import java.util.Random;

public class Perceptron {
	
	private static final int NUM_INPUTS = 3;
	
	private static final int DESIRED_RED = -1;
	private static final int DESIRED_BLUE = 1;
	
	private static final double learningRate = 0.1;
	
	private double[] weights = new double[NUM_INPUTS];

	public Perceptron() {
		setup();
	}
	
	
	public void setup() {
		Random random = new Random();
		for (int i = 0; i < NUM_INPUTS; ++i) {
			weights[i] = random.nextDouble();
		}
	}
	
	
	public int guess(double[] inputs) {
		double weightedSum = 0;
		for (int i = 0; i < inputs.length; ++i) {
			weightedSum += inputs[i] * weights[i];
		}
		return (weightedSum > 0 ? DESIRED_BLUE : DESIRED_RED);
	}
	
	
	public void adjustWeight(double[] inputs, int error) {
		for (int i = 0; i < NUM_INPUTS; ++i) {
			weights[i] += inputs[i] * error * learningRate;
		}
	}
	
}
