package prover.instruction.justification.justifications;

import java.util.List;
import java.util.Set;

import prover.error.logic.logics.GoalManipulationError;
import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.equality.EqualityProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class SubstitutionJustification extends Justification {

	public SubstitutionJustification() {}

	@Override
	public List<Pair<Proposition, Pair<Proposition, Integer>>> getTruths(String loc, TheoremBase base, Proposition prop) throws GoalManipulationError {
		for(Proposition subProp : prop.deconstruct()) {
			boolean found = false;
			for(Proposition truth : base.getTruthsAndEqualities()) {
				if(areSubstitutable(base, subProp, truth)) found = true;
			}
			if(!found) return NewCollection.list();
		}
	
		return null;
	}

	private static <E extends Entity<E>> boolean areSubstitutable(TheoremBase base, Entity<E> one, Entity<E> two) {
		for(Set<EqualityProposition> set : NewCollection.nullToEmpty(one.getDifferenceEqualityPlural(two))) {
			boolean reduced = true;
			for(EqualityProposition eq : set) {
				if(!base.areEqual(eq)) {
					reduced = false;
				}
			}
			if(reduced) return true;
		}

		return false;
	}
}
