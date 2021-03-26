package prover.error.syntax.syntaxes;

import prover.error.syntax.SyntaxError;
import prover.reader.readers.Lexer;

public class DuplicateNameError extends SyntaxError {

	public DuplicateNameError(Lexer lex, String name) {
		super(lex, "Duplicate Name", "The name " + name + " is already in use");
	}

}
