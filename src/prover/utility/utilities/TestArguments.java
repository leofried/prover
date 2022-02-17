package prover.utility.utilities;

import java.util.List;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.Utility;

public class TestArguments extends Utility {

	public static final TestArguments EMPTY = new TestArguments();
	
	private List<TestOperator<Proposition>> predicates;
	private List<TestOperator<Element>> functions;
	private List<TestOperator<Element>> elements;
	
	private TestArguments() {
		this(NewCollection.list(), NewCollection.list(), NewCollection.list());
	}
	
	public TestArguments(TestOperator<Element> element) {
		this(NewCollection.list(), NewCollection.list(), NewCollection.list(element));
	}
	
	public TestArguments(List<TestOperator<Proposition>> predicates, List<TestOperator<Element>> functions) {
		this(predicates, functions, NewCollection.list());
	}
	
	public TestArguments(List<TestOperator<Proposition>> predicates, List<TestOperator<Element>> functions, List<TestOperator<Element>> elements) {
		this.predicates = predicates;
		this.functions = functions;
		this.elements = elements;
	}
	
	public TestArguments(PlatonicArguments arguments) {
		predicates = NewCollection.list();
		for(PlatonicArguments subArguments : arguments.getPredicates()) {
			predicates.add(new TestOperator<Proposition>(subArguments, Proposition.class));
		}
		
		functions = NewCollection.list();
		for(PlatonicArguments subArguments : arguments.getFunctions()) {
			functions.add(new TestOperator<Element>(subArguments, Element.class));
		}
		
		elements = NewCollection.list();
		for(int i=0; i<arguments.getElements(); i++) {
			elements.add(new TestOperator<Element>());
		}
	}

	public List<TestOperator<Proposition>> getPredicates() {
		return NewCollection.list(predicates);
	}
	
	public List<TestOperator<Element>> getFunctions() {
		return NewCollection.list(functions);
	}
	
	public List<TestOperator<Element>> getElements() {
		return NewCollection.list(elements);
	}
	
	public List<Definition<Proposition>> getPredicateDefinitions() {
		return definitionizer(predicates);
	}
	
	public List<Definition<Element>> getFunctionDefinitions() {
		return definitionizer(functions);
	}
	
	public List<Definition<Element>> getElementDefinitions() {
		return definitionizer(elements);
	}
	
	private static <E extends Entity<E>> List<Definition<E>> definitionizer(List<? extends TestOperator<E>> operators) {
		List<Definition<E>> defs = NewCollection.list();
		for(TestOperator<E> operator : operators) {
			defs.add(new Definition<E>(operator));
		}
		return defs;
	}
		
	public PlatonicArguments getArguments() {
		return new PlatonicArguments(NewCollection.list(predicates), NewCollection.list(functions), elements.size());
	}
	
	@Override
	public String toString() {
		String str = new String();
		
		if(getPredicates().size() != 0) str += Constants.PREDICATES.left + getPredicates().toString().substring(1, getPredicates().toString().length() - 1) + Constants.PREDICATES.right;
		if(getFunctions ().size() != 0) str += Constants.FUNCTIONS .left + getFunctions ().toString().substring(1, getFunctions ().toString().length() - 1) + Constants.FUNCTIONS .right;
		if(getElements  ().size() != 0) str += Constants.ELEMENTS  .left + getElements  ().toString().substring(1, getElements  ().toString().length() - 1) + Constants.ELEMENTS  .right;
		
		if(!str.isEmpty()) str = Constants.DEFINITIONS.left + str + Constants.DEFINITIONS.right + Constants.SPACE;
		
		return str;
	}

	public String toTheoremString() {
		String str = new String();
		
		if(getPredicates().size() != 0) {
			str += Constants.PREDICATES.left;
			
			boolean first = true;
			for(TestOperator<Proposition> test : getPredicates()) {
				if(first) first = false;
				else str += Constants.COMMA + Constants.SPACE;
				str += test.toString() + test.getArguments();
			}
			
			str += Constants.PREDICATES.right;
		}
		
		if(getFunctions().size() != 0) {
			str += Constants.FUNCTIONS.left;
			
			boolean first = true;
			for(TestOperator<Element> test : getFunctions()) {
				if(first) first = false;
				else str += Constants.COMMA + Constants.SPACE;
				str += test.toString() + test.getArguments();
			}
			
			str += Constants.FUNCTIONS.right;
		}
		
		if(getElements().size() != 0) {
			str += Constants.ELEMENTS.left;
			
			boolean first = true;
			for(TestOperator<Element> test : getElements()) {
				if(first) first = false;
				else str += Constants.COMMA + Constants.SPACE;
				str += test.toString();
			}
			
			str += Constants.ELEMENTS.right;
		}

		str += Constants.DECLARATION_SEPARATOR;
		
		return str;
	}
	
}
