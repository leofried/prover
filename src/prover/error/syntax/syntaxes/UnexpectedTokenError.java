package prover.error.syntax.syntaxes;

import prover.error.syntax.SyntaxError;
import prover.reader.readers.Lexer;

public class UnexpectedTokenError extends SyntaxError {
	
	public UnexpectedTokenError(Lexer lex) {
		super(lex, "Unexpected token", SyntaxError.surround(lex.next()));
	}
	
}
