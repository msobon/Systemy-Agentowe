package pw;

import java.util.*;
import java.io.*;

import pl.edu.agh.sa.pw.NeuralClassifier;

/**
 * This is the main class containing the body of the game Bot. The name and form
 * are as required by the specification of the competition. Contains standard
 * input/output handling and simple self-made logging utility.
 */
public class PerceptronBot {
	private static boolean learnMode = false;

	/**
	 * This method is called each turn after the input information is parsed.
	 * All of the AI algorithms are contined in static helper classes and called
	 * from within this method. This method changes the game state object taken
	 * as argument and calling it mutliple times on the same game state object
	 * is not thread safe.
	 * 
	 * @param pw
	 *            object containing the parsed game state taken from stdin
	 */
	public static void DoTurn(PlanetWars pw) {
		try {
			if (learnMode) {
				double[] expected = {1.0,1.0}; //aggressive/does not defend
				double[] data = pw.getNElement();
				System.err.println("Values: "+data[0]+" "+data[1]+" "+data[2]);
				NeuralClassifier.loadLearnAndSave("network", data, expected);
			} else {
				double[] result = NeuralClassifier.analyze("network",pw.getNElement());
				System.err.println("Values: "+result[0]+" "+result[1]);
				Conqueror.setFactor(result[0]);
			}
			
			Oracle.sortFleets(pw);
			Oracle.analyzeBattles(pw);
			Protector.calcSafety(pw);
			Conqueror.conquer(pw);
			Conqueror.steal(pw);
			Explorer.explore(pw);
			Strategist.microShift(pw);
		} catch (Exception e) {
			log("exception" + e);
		}
	}

	/**
	 * Infinite loop waiting for incoming game state. Creates a new game state
	 * object (PlanetWars) which parses the stdin.
	 */
	public static void main(String[] args) {
		for(String a : args) if (a.equals("learn")) learnMode = true;
	
		String line = "";
		String message = "";
		int c;
		try {
			while ((c = System.in.read()) >= 0) {
				switch (c) {
				case '\n':
					if (line.equals("go")) {
						PlanetWars pw = new PlanetWars(message);
						DoTurn(pw);
						pw.FinishTurn();
						message = "";
					} else {
						message += line + "\n";
					}
					line = "";
					break;
				default:
					line += (char) c;
					break;
				}
			}
		} catch (Exception e) {
			log("exception" + e);
		}
	}

	private static FileWriter logger = null;

	/**
	 * Simple self-made logging mechanism. Completely static, writing to
	 * mylog.txt file. Used only for troublesome debugging.
	 * 
	 * @param msg
	 *            the full text to be logged
	 */
	public static void log(String msg) {
		try {
			if (logger == null)
				logger = new FileWriter("mylog.txt");
			logger.write(msg);
			logger.write("\n");
			logger.flush();
		} catch (Exception e) {
		}
	}
}
