package prover.instruction.justification.justifications;

import java.util.Set;

import prover.error.logic.logics.AmbiguousDefinitionError;
import prover.error.logic.logics.GoalManipulationError;
import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.flippable.equality.EqualityProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class SubstitutionJustification extends Justification {

	private static SubstitutionJustification instance = null;

	public static SubstitutionJustification getInstance() {
		if(instance == null) instance = new SubstitutionJustification();
		return instance;
	}

	private SubstitutionJustification() {}

	@Override
	public Set<Pair<Proposition, Proposition>> getTruths(String loc, TheoremBase base, Proposition prop) throws GoalManipulationError, AmbiguousDefinitionError {
		for(Proposition truth : base.getTruthsAndEqualities()) {
			if(areSubstitutable(base, prop, truth)) return null;
		}

	/*	if(prop.getSubstitutionPairs() != null) {	BAD
			boolean reduced = true;
			for(Pair<Element, Element> pair : prop.getSubstitutionPairs()) {
				if(!areSubstitutable(base, pair.left, pair.right)) reduced = false;
			}
			if(reduced) return null;
		}
	*/

		return NewCollection.set();
	}

	private static <E extends Entity<E>> boolean areSubstitutable(TheoremBase base, Entity<E> one, Entity<E> two) {
		for(Set<EqualityProposition> set : NewCollection.nullToEmpty(one.getDifferenceEqualityPlural(two))) {
			boolean reduced = true;
			for(EqualityProposition eq : set) {
				if(!base.getEqualitySet(eq.getLeft()).contains(eq.getRight())){
					reduced = false;
				}
			}
			if(reduced) return true;
		}

		return false;
	}
}
