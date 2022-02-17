package prover.error.syntax.syntaxes;

import prover.error.syntax.SyntaxError;
import prover.reader.readers.Lexer;
import prover.utility.utilities.FileType;

public class CircularReferenceError extends SyntaxError {

	public CircularReferenceError(Lexer lex, String name) {
		super(lex, "Circular Reference", FileType.append(lex.getFileName()) + " and " + name + " are each dependent on each other");
	}

}
