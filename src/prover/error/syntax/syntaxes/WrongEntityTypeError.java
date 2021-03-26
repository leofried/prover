package prover.error.syntax.syntaxes;

import prover.error.syntax.SyntaxError;
import prover.reader.readers.Lexer;
import prover.structure.regular.entity.Entity;
import prover.utility.utilities.NewCollection;

public class WrongEntityTypeError extends SyntaxError {

	public WrongEntityTypeError(Lexer lex, Entity<?> entity) {
		super(lex, "Wrong entity type", SyntaxError.surround(entity.toString()) + " is not " + stringify(flip(entity), true));
	}

	private static StringType flip(Entity<?> entity) {
		return NewCollection.chooseEntityType(entity.getClass(), StringType.ELEMENT, StringType.PROPOSITION);
	}

}
