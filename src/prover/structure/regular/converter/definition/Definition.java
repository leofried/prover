package prover.structure.regular.converter.definition;

import java.util.List;
import java.util.Set;

import prover.structure.Structure;
import prover.structure.regular.converter.Converter;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;

public class Definition<E extends Entity<E>> extends Converter<Definition<E>, E> {
	
	public Definition(E entity) {
		this(new TestArguments(), entity);
	}
	
	public Definition(TestArguments tests, E entity) {
		super(tests, entity);
	}
	
	
	public Definition(List<Operator<Proposition>> predicates, List<Operator<Element>> functions, E entity) {
		this(predicates, functions, new TestArguments(new PlatonicArguments(predicates, functions)), entity);
	}
	
	private Definition(List<Operator<Proposition>> predicates, List<Operator<Element>> functions, TestArguments arguments, E entity) {
		super(arguments, entity.substituteOperators(new OperatorDefinitionMap(predicates, arguments.getPredicateDefinitions()))
							   .substituteOperators(new OperatorDefinitionMap(functions,  arguments.getFunctionDefinitions())));
	}

	
	public Definition(Operator<E> operator) {
		this(operator, new TestArguments(operator.getArguments()));
	}
	
	private Definition(Operator<E> operator, TestArguments arguments) {
		super(arguments, operator.convert(arguments));
	}

	@Override
	protected final Definition<E> newStructure(TestArguments tests, List<Structure> subs) {
		return new Definition<E>(tests, (E) subs.get(0));
	}
	
	@Override
	public final Class<E> getConverterClass(){
		return NewCollection.chooseEntityType((Class<E>) getSubstructures().get(0).getClass(), (Class<E>) Proposition.class, (Class<E>) Element.class);
		/*
		 * type erasure lol
		 */
	}
	
	@Override
	public PlatonicArguments getArguments() {
		return getTests().getArguments();
	}
	
	@Override
	public final E convert(List<Definition<Proposition>> predicates, List<Definition<Element>> functions, List<Element> elements) {
		return ((E) getSubstructures().get(0))
				.substituteOperators(new OperatorDefinitionMap(NewCollection.list(getTests().getPredicates()), predicates))
				.substituteOperators(new OperatorDefinitionMap(NewCollection.list(getTests().getFunctions()), functions))
				.substituteOperators(new OperatorDefinitionMap(NewCollection.list(getTests().getElements()), convertHelper(elements)));
	}
	
	private static List<Definition<Element>> convertHelper(List<Element> elements) {
		List<Definition<Element>> defs = NewCollection.list();
		for(Element element : elements) {
			defs.add(new Definition<Element>(new TestArguments(), element));
		}
		return defs;
	}
	
	@Override
	public Set<Definition<?>> getAllDefinitionsHelper() {
		return NewCollection.set(this);
	}

	@Override
	public String toString() {
		return getArguments() + " " + getTests() +  " : " + getSubstructures().get(0); 
	}
}
