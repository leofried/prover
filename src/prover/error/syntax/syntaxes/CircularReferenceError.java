package prover.error.syntax.syntaxes;

import prover.error.syntax.SyntaxError;
import prover.reader.readers.Lexer;

public class CircularReferenceError extends SyntaxError {

	public CircularReferenceError(Lexer lex, String name) {
		super(lex, "Circular Reference", lex.getFileName() + " and " + name + " are each dependent on each other");
	}

}
