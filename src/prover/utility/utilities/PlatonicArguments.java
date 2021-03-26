package prover.utility.utilities;

import java.util.List;
import java.util.Objects;

import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.standard.test.element.TestElement;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.Utility;

public class PlatonicArguments extends Utility {

	public static final PlatonicArguments BINARY_PROPOSITION_OPERATOR_ARGUMENTS = new PlatonicArguments(NewCollection.list(new PlatonicArguments(), new PlatonicArguments()), NewCollection.list(), 0);
	public static final PlatonicArguments  UNARY_PROPOSITION_OPERATOR = new PlatonicArguments(NewCollection.list(new PlatonicArguments()                         ), NewCollection.list(), 0);
	
	private List<PlatonicArguments> predicates;
	private List<PlatonicArguments> functions;
	private int elements;
	
	public PlatonicArguments() {
		this(0);
	}
	
	public PlatonicArguments(int elements) {
		this(NewCollection.list(), NewCollection.list(), elements);
	}
	
	public PlatonicArguments(List<PlatonicArguments> predicates, List<PlatonicArguments> functions, int elements) {
		this.predicates = predicates;
		this.functions = functions;
		this.elements = elements;
	}
	
	public PlatonicArguments(List<Operator<Proposition>> predicates, List<Operator<Element>> functions) {
		this(predicates, functions, NewCollection.list());
	}
	
	public PlatonicArguments(List<Operator<Proposition>> realPredicates, List<Operator<Element>> realFunctions,	List<TestElement> realElements) {
		predicates = NewCollection.list();
		for(Operator<Proposition> predicate : realPredicates) {
			predicates.add(predicate.getArguments());
		}
		
		functions = NewCollection.list();
		for(Operator<Element> function : realFunctions) {
			functions.add(function.getArguments());
		}
		
		elements = realElements.size();
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
