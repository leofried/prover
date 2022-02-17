package prover.reader;

/**
 * Copywrite Leo Fried 2020 - 2021
 */

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.reader.readers.Executer;
import prover.reader.readers.Lexer;
import prover.utility.utilities.Constants;
import prover.utility.utilities.Logger;

public abstract class Reader {

	public static final boolean DEBUG = false;

	public static void main(String[] args) throws InterruptedException {
		long time = System.currentTimeMillis();
		
		Executer exec = new Executer();

		try {
			exec.compile(new Lexer(), Constants.FILE_NAME);
		} catch(SyntaxError e) {
			if(Reader.DEBUG) {
				e.printStackTrace();
			}else {
				Logger.log(e.getMessage());
			}
			
			return;
		}

		Logger.log("Compilied at " + (System.currentTimeMillis() - time) + " milliseconds");
		
		try {
			exec.run(Constants.FILE_NAME);
		} catch (LogicError e) {
			if(Reader.DEBUG) {
				e.printStackTrace();
			}else {
				Logger.log(e.getMessage());
			}
		}

		Logger.log("Completed at " + (System.currentTimeMillis() - time) + " milliseconds");
	}
}
