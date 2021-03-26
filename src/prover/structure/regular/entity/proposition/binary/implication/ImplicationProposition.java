package prover.structure.regular.entity.proposition.binary.implication;

import java.util.List;
import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.Structure;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.BinaryProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.Pair;
import prover.utility.utilities.TestArguments;

public class ImplicationProposition extends BinaryProposition<Proposition> {
	
	public ImplicationProposition(Proposition left, Proposition right) {
		super(left, right);
	}

	@Override
	public final BinaryProposition<Proposition> newStructure(TestArguments tests, List<Structure> subs) {
		return new ImplicationProposition(getLeft(subs), getRight(subs));
	}

	@Override
	public final Set<Pair<Proposition, Proposition>> getAssumptionSet() {
		return NewCollection.set(NewCollection.pair(getLeft(), getRight()));
	}

	@Override
	protected final String stringName() {
		return "->";
	}
	
	@Override
	protected Set<OperatorDefinitionMap> getSetOfMapsThatProveDeep(Set<TestOperator<?>> testOperators, TheoremBase base, Set<Proposition> targets) {		
		return OperatorDefinitionMap.combineMaps(getLeft().getSetOfMapsThatMakeTrue(testOperators, base), getRight().getSetOfMapsThatProve(testOperators, base, targets));
	}
}
