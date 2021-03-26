package prover.structure.regular.entity.proposition;

import java.util.List;
import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.binary.flippable.junction.junctions.ConjunctionProposition;
import prover.structure.regular.entity.proposition.binary.implication.ImplicationProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.Pair;
import prover.utility.utilities.TestArguments;

public abstract class Proposition extends Entity<Proposition> {

	public static final Proposition FALSE = Operator.FALSE.convert();
	public static final Proposition TRUE  = Operator.TRUE .convert();

	public static Proposition iff(Proposition a, Proposition b) {
		return new ConjunctionProposition(new ImplicationProposition(a, b), new ImplicationProposition(b, a));
	}

	public Proposition(TestArguments arguments, List<Structure> subs) {
		super(arguments, subs);
	}
	
	public final Proposition not() {
		return new ImplicationProposition(this, FALSE);
	}

	@Override
	public final Set<Definition<?>> getAllDefinitionsHelper() {		
		Set<Definition<?>> set = NewCollection.set(new Definition<Proposition>(this));
		set.addAll(getAllDefinitionsHelperHelper());
		return set;
	}	
	
	protected Set<Definition<Proposition>> getAllDefinitionsHelperHelper(){
		return NewCollection.set();
	}

	public Set<Proposition> deconstruct(){
		return NewCollection.set(this);
	}

	public Proposition applyElementUniversal(Element element) {
		return null;
	}

	public Proposition applyElementExistential(Element element) {
		return null;
	}
	
	public Set<Pair<Proposition, Proposition>> getAssumptionSet(){
		return null;
	}
	
	
	
	
	public final boolean isTrue(TheoremBase base) {
		return !getSetOfMapsThatMakeTrue(NewCollection.set(), base).isEmpty();
	}
	
	public final Set<OperatorDefinitionMap> getSetOfMapsThatMakeTrue(Set<TestOperator<?>> testOperators, TheoremBase base){
		Set<OperatorDefinitionMap> setOfMaps = NewCollection.set();
		
		for(Proposition truth : base.getTruthsAndEqualities()) {
			setOfMaps.addAll(assignTestOperators(truth, testOperators));
		}
		
		setOfMaps.addAll(getSetOfMapsThatMakeTrueDeep(testOperators, base));
		return setOfMaps;
	}
	
	protected Set<OperatorDefinitionMap> getSetOfMapsThatMakeTrueDeep(Set<TestOperator<?>> testOperators, TheoremBase base) {
		return NewCollection.set();
	}
	
	public final boolean doesThisProve(TheoremBase base, Set<Proposition> targets) {
		return !getSetOfMapsThatProve(NewCollection.set(), base, targets).isEmpty();
	}
	
	public final Set<OperatorDefinitionMap> getSetOfMapsThatProve(Set<TestOperator<?>> testOperators, TheoremBase base, Set<Proposition> targets) {
		Set<OperatorDefinitionMap> setOfMaps = NewCollection.set();
		
		for(Proposition target : targets) {
			setOfMaps.addAll(assignTestOperators(target, testOperators));
		}
		
		setOfMaps.addAll(getSetOfMapsThatProveDeep(testOperators, base, targets));
		return setOfMaps;
	}

	protected Set<OperatorDefinitionMap> getSetOfMapsThatProveDeep(Set<TestOperator<?>> testOperators, TheoremBase base, Set<Proposition> targets) {
		return NewCollection.set();
	}
	
}