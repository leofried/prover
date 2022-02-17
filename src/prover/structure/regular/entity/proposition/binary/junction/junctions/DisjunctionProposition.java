package prover.structure.regular.entity.proposition.binary.junction.junctions;

import java.util.List;
import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.Structure;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.junction.JunctionProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.Pair;
import prover.utility.utilities.TestArguments;

public class DisjunctionProposition extends JunctionProposition {

	public DisjunctionProposition(Proposition left, Proposition right) {
		super(left, right);
	}
	
	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return new DisjunctionProposition(getLeft(subs), getRight(subs));
	}
	
	@Override
	public Proposition not() {
		return new ConjunctionProposition(getLeft().not(), getRight().not());
	}	
	
	@Override
	public final List<Pair<Proposition, Proposition>> getAssumptionList() {
		return NewCollection.list(
				NewCollection.pair(getLeft().not(), getRight()),
				NewCollection.pair(getRight().not(), getLeft()));
	}
	
	@Override
	protected final Set<OperatorDefinitionMap> getSetOfMapsThatMakeTrueDeep(TheoremBase base, Set<TestOperator<?>> testOperators) {
		Set<OperatorDefinitionMap> set = NewCollection.set();
		for(Proposition prop : NewCollection.set(getLeft(), getRight())) {
			set.addAll(prop.getSetOfMapsThatMakeTrue(base, testOperators));
		}
		return set;
	}
	
	@Override
	protected final String stringName() {
		return Constants.OR;
	}
}
