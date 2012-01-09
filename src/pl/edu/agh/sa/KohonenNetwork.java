package pl.edu.agh.sa;

import java.util.ArrayList;
import java.util.Collections;

import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.learning.KohonenLearning;

public class KohonenNetwork {
	static Kohonen neuralNetwork;
	static TrainingSet trainingSet;

	public KohonenNetwork(int inputs, int outputs) {
		neuralNetwork = new Kohonen(inputs, outputs);
		trainingSet = new TrainingSet();
	}
	
	public void train(double[] trainingVector){
		trainingSet.addElement(new TrainingElement(trainingVector));
	}
	
	public int analyzeWithKohonenNetwork(double[] inputVector) {
		KohonenLearning kohonenLearning = new KohonenLearning();
		kohonenLearning.setNeuralNetwork(neuralNetwork);

		kohonenLearning.learn(trainingSet);
		neuralNetwork.setInput(inputVector);
		neuralNetwork.calculate();
		ArrayList<Double> solution =  new ArrayList<Double>();
		for (double d : neuralNetwork.getOutput()) {
			solution.add(d);
		}
		
		return solution.indexOf(Collections.max(solution));
		
	}

	public static void main(String[] args) {
		KohonenNetwork network =  new KohonenNetwork(1, 3);
		double [] inputVector =   new double []{1.0};
		
		System.out.println(network.analyzeWithKohonenNetwork(inputVector));
	}
}
