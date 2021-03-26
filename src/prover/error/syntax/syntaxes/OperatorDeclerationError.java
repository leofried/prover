package prover.error.syntax.syntaxes;

import prover.error.syntax.SyntaxError;
import prover.reader.readers.Lexer;

public class OperatorDeclerationError extends SyntaxError {
	
	public OperatorDeclerationError(Lexer lex, String msg) {
		super(lex, "Illegal operator decleration", msg);
	}
	
}
