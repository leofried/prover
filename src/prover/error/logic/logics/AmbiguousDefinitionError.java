package prover.error.logic.logics;

import prover.error.logic.LogicError;

public class AmbiguousDefinitionError extends LogicError {

	public AmbiguousDefinitionError(String loc) {
		super(loc, "Theorem definitions are ambiguous");
	}
	
}
