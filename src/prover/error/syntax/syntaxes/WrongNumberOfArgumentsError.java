package prover.error.syntax.syntaxes;

import prover.error.syntax.SyntaxError;
import prover.reader.readers.Lexer;

public class WrongNumberOfArgumentsError extends SyntaxError {

	public WrongNumberOfArgumentsError(Lexer lex, int arguments) {
		super(lex, "Wrong number of arguments", arguments + " expected");
	}

}
