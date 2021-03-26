package prover.utility.utilities;

import java.util.List;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.converter.operator.standard.test.element.TestElement;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.Utility;

public class TestArguments extends Utility {

	private List<TestOperator<Proposition>> predicates;
	private List<TestOperator<Element>> functions;
	private List<TestElement> elements;
	
	public TestArguments() {
		this(NewCollection.list(), NewCollection.list(), NewCollection.list());
	}
	
	public TestArguments(TestElement element) {
		this(NewCollection.list(), NewCollection.list(), NewCollection.list(element));
	}
	
	public TestArguments(List<TestOperator<Proposition>> predicates, List<TestOperator<Element>> functions) {
		this(predicates, functions, NewCollection.list());
	}
	
	public TestArguments(List<TestOperator<Proposition>> predicates, List<TestOperator<Element>> functions, List<TestElement> elements) {
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
			elements.add(new TestElement());
		}
	}

	public List<TestOperator<Proposition>> getPredicates() {
		return NewCollection.list(predicates);
	}
	
	public List<TestOperator<Element>> getFunctions() {
		return NewCollection.list(functions);
	}
	
	public List<TestElement> getElements() {
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
	
	public static <E extends Entity<E>> List<Definition<E>> definitionizer(List<? extends TestOperator<E>> operators) {
		List<Definition<E>> defs = NewCollection.list();
		for(TestOperator<E> operator : operators) {
			defs.add(new Definition<E>(operator));
		}
		return defs;
	}
		
	public PlatonicArguments getArguments() {
		return new PlatonicArguments(NewCollection.list(predicates), NewCollection.list(functions), elements);
	}
	
}
