package prover.reader;

/**
 * Copywrite Leo Fried 2020 - 2021
 */

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.reader.readers.Executer;
import prover.reader.readers.Lexer;

public abstract class Reader {

	public static final boolean DEBUG = false;
	private static final String FILE_NAME = "Main.proof";

	public static void main(String[] args) {
		long time = System.currentTimeMillis();

		Executer exec = new Executer();

		try {
			exec.compile(new Lexer(), FILE_NAME);
		} catch(SyntaxError e) {
			if(Reader.DEBUG) {
				e.printStackTrace();
			}else {
				System.out.println(e.getMessage());
			}
			return;
		}

		System.out.println("Compilied at " + (System.currentTimeMillis() - time) + " milliseconds");
		
		try {
			exec.run();
		} catch (LogicError e) {
			if(Reader.DEBUG) {
				e.printStackTrace();
			}else {
				System.out.println(e.getMessage());
			}
		}

		System.out.println(System.currentTimeMillis() - time + " milliseconds");
	}



}
