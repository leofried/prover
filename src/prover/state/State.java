package prover.state;

public abstract class State {

	private String fileName;
	
	public State(String fileName) {
		this.fileName = fileName;
	}
	
	public String getName() {
		return fileName;
	}
	
}
