package prover.structure.regular.entity.proposition.binary.flippable.junction.junctions;

import java.util.List;
import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.flippable.junction.JunctionProposition;
import prover.structure.regular.entity.proposition.binary.implication.ImplicationProposition;
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
	public final Set<Proposition> deconstruct() {
		return NewCollection.set(
				new ImplicationProposition(getLeft().not(), getRight()),
				new ImplicationProposition(getRight().not(), getLeft()));
	}
	
	private Set<Proposition> extendedDeconstruct(){
		Set<Proposition> set = deconstruct();
		set.add(getLeft());
		set.add(getRight());
		return set;
	}
	
	
	@Override
	protected final Set<Definition<Proposition>> getAllDefinitionsHelperHelper() {
		Set<Definition<Proposition>> set = NewCollection.set();
		for(Proposition prop : deconstruct()) {
			set.add(new Definition<Proposition>(prop));
		}
		return set;
	}
	
	@Override
	public final Set<Pair<Proposition, Proposition>> getAssumptionSet() {
		return NewCollection.set(
				NewCollection.pair(getLeft().not(), getRight()),
				NewCollection.pair(getRight().not(), getLeft()));
	}

	@Override
	protected final String stringName() {
		return "OR";
	}
	
	@Override
	protected final Set<OperatorDefinitionMap> getSetOfMapsThatMakeTrueDeep(Set<TestOperator<?>> testOperators, TheoremBase base) {
		Set<OperatorDefinitionMap> set = NewCollection.set();
		for(Proposition prop : extendedDeconstruct()) {
			set.addAll(prop.getSetOfMapsThatMakeTrue(testOperators, base));
		}
		return set;
	}
	
}
