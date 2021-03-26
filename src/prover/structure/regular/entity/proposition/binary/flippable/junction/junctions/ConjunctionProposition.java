package prover.structure.regular.entity.proposition.binary.flippable.junction.junctions;

import java.util.List;
import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.Structure;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.flippable.junction.JunctionProposition;
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
	public final Set<Proposition> deconstruct() {
		return NewCollection.set(getLeft(), getRight());
	}
	
	@Override
	protected String stringName() {
		return "AND";
	}
	
	@Override
	protected final Set<OperatorDefinitionMap> getSetOfMapsThatMakeTrueDeep(Set<TestOperator<?>> testOperators, TheoremBase base) {
		return OperatorDefinitionMap.combineMaps(getLeft().getSetOfMapsThatMakeTrue(testOperators, base), getRight().getSetOfMapsThatMakeTrue(testOperators, base));
	}

}
