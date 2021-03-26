package prover.structure.regular.entity.proposition.binary.flippable.junction;

import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.flippable.FlippableProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;

public abstract class JunctionProposition extends FlippableProposition<Proposition> {

	public JunctionProposition(Proposition left, Proposition right) {
		super(left, right);
	}
	
	@Override
	protected Set<OperatorDefinitionMap> getSetOfMapsThatProveDeep(Set<TestOperator<?>> testOperators, TheoremBase base, Set<Proposition> targets) {		
		Set<OperatorDefinitionMap> set = NewCollection.set();
		for(Proposition prop : deconstruct()) {
			set.addAll(prop.getSetOfMapsThatProve(testOperators, base, targets));
		}
		return set;
	}

}
