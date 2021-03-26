package prover.error;

public abstract class Error extends Exception {
	
	public Error(String type, String loc, String msg) {
		super("\n" + type + " Error\n" + loc + "\n" + msg + '.');
	}

}

