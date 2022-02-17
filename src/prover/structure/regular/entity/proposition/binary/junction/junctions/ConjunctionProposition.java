package prover.structure.regular.entity.proposition.binary.junction.junctions;

import java.util.List;
import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.Structure;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.junction.JunctionProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.TestArguments;

public class ConjunctionProposition extends JunctionProposition {

	public ConjunctionProposition(Proposition left, Proposition right) {
		super(left, right);
	}

	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return new ConjunctionProposition(getLeft(subs), getRight(subs));
	}
	
	@Override
	public Proposition not() {
		return new DisjunctionProposition(getLeft().not(), getRight().not());
	}
	
	@Override
	public final Set<Proposition> deconstruct() {
		return NewCollection.set(getLeft().deconstruct(), getRight().deconstruct());
	}
	
	@Override
	public final Proposition applyElementExistential(Element element) {
		return getRight().applyElementExistential(element);
	}
	
	@Override
	protected Set<OperatorDefinitionMap> getSetOfMapsThatProveDeep(TheoremBase base, Set<TestOperator<?>> testOperators, Set<Proposition> targets) {		
		Set<OperatorDefinitionMap> set = NewCollection.set();
		for(Proposition prop : NewCollection.list(getLeft(), getRight())) {
			set.addAll(prop.getSetOfMapsThatProve(base, testOperators, targets));
		}
		return set;
	}

	@Override
	protected String stringName() {
		return Constants.AND;
	}
	
}
