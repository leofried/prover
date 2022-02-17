package prover.structure.regular.entity.proposition.other;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.interfaces.OperatorEntity;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.TestArguments;

public class PredicateProposition extends Proposition implements OperatorEntity<Proposition> {

	public PredicateProposition(Operator<Proposition> operator, List<Definition<Proposition>> predicates, List<Definition<Element>> functions, List<Element> elements) {
		this(NewCollection.list(operator, NewCollection.list(predicates, functions, elements)));
	}
	
	private PredicateProposition(List<Structure> subs) {
		super(TestArguments.EMPTY, subs);
	}
	 
	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return new PredicateProposition(subs);
	}
	
	@Override
	public List<Structure> getSubsHelper() {
		return getSubstructures();
	}
	
	@Override
	public Proposition not() {
		return new NegationProposition(this);
	}
	
	@Override
	public final String toString() {
		return toStringHelper();
	}

}
