package prover.error.syntax.syntaxes;

import prover.error.syntax.SyntaxError;
import prover.reader.readers.Lexer;

public class NoSuchNameError extends SyntaxError {

	public NoSuchNameError(Lexer lex, String name, StringType type) {
		super(lex, "No such " + stringify(type, false), surround(name));
	}

}
