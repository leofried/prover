package prover.structure.regular.entity.proposition;

import java.util.List;
import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.Structure;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.binary.junction.junctions.ConjunctionProposition;
import prover.structure.regular.entity.proposition.binary.junction.junctions.DisjunctionProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.Pair;
import prover.utility.utilities.TestArguments;

public abstract class Proposition extends Entity<Proposition> {

	public static final boolean SMART_LEARN = true;

	public static Proposition implies(Proposition a, Proposition b) {
		return new DisjunctionProposition(a.not(), b);
	}

	public static Proposition iff(Proposition a, Proposition b) {
		return new ConjunctionProposition(implies(a, b), implies(b, a));
	}

	public static Set<OperatorDefinitionMap> learn(Proposition ant, Proposition con, Set<TestOperator<?>> testOperators, TheoremBase base, Set<Proposition> targets){
		if(!SMART_LEARN) return OperatorDefinitionMap.combineMaps(ant.not().getSetOfMapsThatMakeTrue(base, testOperators), con.getSetOfMapsThatProve(base, testOperators, targets));

		Set<OperatorDefinitionMap> set = NewCollection.set();
		for(OperatorDefinitionMap conMap : con.getSetOfMapsThatProve(base, testOperators, targets)) {
			Proposition newAnt = ant.substituteOperators(conMap);
			for(OperatorDefinitionMap antMap : newAnt.getSetOfMapsThatMakeTrue(base, testOperators)) {
				set.add(OperatorDefinitionMap.combineMaps(conMap, antMap));
			}
		}

		return set;
	}




	public Proposition(TestArguments arguments, List<Structure> subs) {
		super(arguments, subs);
	}

	public abstract Proposition not();




	public Set<Proposition> deconstruct(){
		return NewCollection.set(this);
	}

	public List<Pair<Proposition, Proposition>> getAssumptionList() {
		return NewCollection.list();
	}

	public Proposition applyElementUniversal(Element element) {
		return null;
	}

	public Proposition applyElementExistential(Element element) {
		return null;
	}


	public final boolean isTrue(TheoremBase base) {
		return !getSetOfMapsThatMakeTrue(base, NewCollection.set()).isEmpty();
	}

	public final Set<OperatorDefinitionMap> getSetOfMapsThatMakeTrue(TheoremBase base, Set<TestOperator<?>> testOperators){
		Set<OperatorDefinitionMap> setOfMaps = NewCollection.set(new OperatorDefinitionMap());

		for(Proposition prop : deconstruct()) {
			Set<OperatorDefinitionMap> miniSetOfMaps = NewCollection.set();
			for(Proposition truth : base.getTruthsAndEqualities()) {
				miniSetOfMaps.addAll(prop.assignTestOperators(truth, testOperators));
			}
	
			miniSetOfMaps.addAll(prop.getSetOfMapsThatMakeTrueDeep(base, testOperators));
			setOfMaps = OperatorDefinitionMap.combineMaps(setOfMaps, miniSetOfMaps);
		}
		return setOfMaps;
	}

	protected Set<OperatorDefinitionMap> getSetOfMapsThatMakeTrueDeep(TheoremBase base, Set<TestOperator<?>> testOperators) {
		return NewCollection.set();
	}

	
	public final Boolean doesThisProve(TheoremBase base, Set<Proposition> targets) {		
		return !getSetOfMapsThatProve(base, NewCollection.set(), targets).isEmpty();
	}

	public final Set<OperatorDefinitionMap> getSetOfMapsThatProve(TheoremBase base, Set<TestOperator<?>> testOperators, Set<Proposition> targets) {
		Set<OperatorDefinitionMap> setOfMaps = NewCollection.set();

		for(Proposition target : targets) {
			setOfMaps.addAll(assignTestOperators(target, testOperators));
		}
		
		for(Pair<Proposition, Proposition> pair : getAssumptionList()) {
			setOfMaps.addAll(learn(pair.left, pair.right, testOperators, base, targets));
		}

		setOfMaps.addAll(getSetOfMapsThatProveDeep(base, testOperators, targets));
		return setOfMaps;
	}

	protected Set<OperatorDefinitionMap> getSetOfMapsThatProveDeep(TheoremBase base, Set<TestOperator<?>> testOperators, Set<Proposition> targets) {
		return NewCollection.set();
	}

}