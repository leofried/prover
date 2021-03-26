package prover.error.logic.logics;

import prover.error.logic.LogicError;

public class CannotResolveError extends LogicError {

	public CannotResolveError(String loc, String name) {
		super(loc, "Unable to resolve" + (name.isEmpty() ? "" :  " ") + name);
	}

}
