package pw;

import java.util.*;
import java.io.*;
import org.drools.*;
import org.drools.builder.*;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.*;

/**
 * This is the main class containing the body of the game Bot.
 * The name and form are as required by the specification of the competition.
 * Contains standard input/output handling and simple self-made logging utility.
 */
public class RuleBot {

	private static KnowledgeBase kbase;
	
	/**
	 * This method is called each turn after the input information is parsed.
	 * It invokes the semantic engine to analyze the facts.
	 * This method changes the game state object taken as argument and calling it
	 * mutliple times on the same game state object is not thread safe.
	 * @param pw object containing the parsed game state taken from stdin
	 */
    public static void DoTurn(PlanetWars pw) {
    	try {
			StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
			ksession.setGlobal("game", pw);
			List facts = new ArrayList();
				facts.add(pw);
				facts.addAll(pw.planets);
				facts.addAll(pw.fleets);
			ksession.execute(facts);
		} catch (Exception e) {
	    	log("exception" + e);
		}
    }

	/**
	 * Infinite loop waiting for incoming game state.
	 * Creates a new game state object (PlanetWars) which parses the stdin.
	 */
    public static void main(String[] args) {
	String line = "";
	String message = "";
	int c;
	try {
		readKnowledgeBase();
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
		    line += (char)c;
		    break;
		}
	    }
	} catch (Exception e) {
	    log("exception" + e);
	}
    }
    
    private static FileWriter logger = null;
    /**
     * Simple self-made logging mechanism.
     * Completely static, writing to mylog.txt file.
     * Used only for troublesome debugging.
     * @param msg the full text to be logged
     */
	public static void log(String msg) {
		try {
			if (logger == null) logger = new FileWriter("mylog.txt");
			logger.write(msg);
			logger.write("\n");
			logger.flush();
		} catch (Exception e){}
	}
	
	private static void readKnowledgeBase() throws Exception {
		log("Reading Knowledge Base");
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("rulebase.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error: errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		log("Finished reading.");
	}
}

