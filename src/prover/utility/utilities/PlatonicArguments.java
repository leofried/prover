package prover.utility.utilities;

import java.util.List;
import java.util.Objects;

import prover.utility.Utility;
import prover.utility.interfaces.Argumented;

public class PlatonicArguments extends Utility implements Argumented {

	public static final PlatonicArguments EMPTY = new PlatonicArguments();
	
	private List<PlatonicArguments> predicates;
	private List<PlatonicArguments> functions;
	private int elements;
	
	private PlatonicArguments() {
		this(0);
	}
	
	public PlatonicArguments(int elements) {
		this(NewCollection.list(), NewCollection.list(), elements);
	}
	
	public PlatonicArguments(List<? extends Argumented> predicates, List<? extends Argumented> functions, int elements) {
		this.predicates = NewCollection.list();
		for(Argumented predicate : predicates) {
			this.predicates.add(predicate.getArguments());
		}
		
		this.functions = NewCollection.list();
		for(Argumented function : functions) {
			this.functions.add(function.getArguments());
		}
		
		this.elements = elements;
	}

	public List<PlatonicArguments> getPredicates(){
		return NewCollection.list(predicates);
	}
	
	public List<PlatonicArguments> getFunctions(){
		return NewCollection.list(functions);
	}
	
	public int getElements() {
		return elements;
	}
	
	@Override
	public PlatonicArguments getArguments() {
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		PlatonicArguments arg = (PlatonicArguments) obj;
		return getElements() == arg.getElements() && getPredicates().equals(arg.getPredicates()) && getFunctions().equals(arg.getFunctions());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPredicates(), getFunctions(), getElements());
	}
	
	@Override
	public String toString() {
		return  Constants.PREDICATES.left + getPredicates().toString().substring(1, getPredicates().toString().length() - 1) + Constants.PREDICATES.right
			  + Constants.FUNCTIONS .left + getFunctions() .toString().substring(1, getFunctions() .toString().length() - 1) + Constants.FUNCTIONS .right
			  + Constants.ELEMENTS  .left + getElements() + Constants.ELEMENTS.right;
	}
}
