package prover.error.logic.logics;

import prover.error.logic.LogicError;

public class CannotReduceError extends LogicError{

	public CannotReduceError(String loc) {
		super(loc, "Unable to reduce to truth");
	}
	
}
