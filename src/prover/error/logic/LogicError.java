package prover.error.logic;

import prover.error.Error;

public abstract class LogicError extends Error {

	public LogicError(String loc, String msg) {
		super("Logic", loc, msg);
	}

}
