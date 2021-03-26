package prover.structure.regular.entity.element;

import java.util.List;
import java.util.Set;

import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.interfaces.OperatorEntity;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.TestArguments;

/**
 * {@code Element} is an abstract class representing a single element in the mathematical universe.
 */
public class Element extends Entity<Element> implements OperatorEntity<Element> {
	
	public Element(Operator<Element> operator) {
		this(operator, NewCollection.list(), NewCollection.list(), NewCollection.list());
	}
	
	public Element(Operator<Element> operator, List<Definition<Proposition>> predicates, List<Definition<Element>> functions, List<Element> elements) {
		super(new TestArguments(), NewCollection.list(operator, NewCollection.list(predicates, functions, elements)));
	}
	
	private Element(List<Structure> subs) {
		super(new TestArguments(), subs);
	}
	 
	@Override
	protected final Element newStructure(TestArguments tests, List<Structure> subs) {
		return new Element(subs);
	}
	
	@Override
	public List<Structure> getSubsHelper() {
		return getSubstructures();
	}
	
	@Override
	public Set<Definition<?>> getAllDefinitionsHelper() {
		return NewCollection.set(new Definition<Element>(this));
	}
	
	@Override
	public final String toString() {
		return toStringHelper();
	}
}
