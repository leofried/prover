package prover.instruction.justification.justifications;

import java.util.Set;

import prover.error.logic.logics.GoalManipulationError;
import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class SorryJustification extends Justification {

	private static SorryJustification instance = null;

	public static SorryJustification getInstance() {
		if(instance == null) instance = new SorryJustification();
		return instance;
	}

	private SorryJustification() {}
	
	@Override
	public Set<Pair<Proposition, Proposition>> getTruths(String loc, TheoremBase state, Proposition prop) throws GoalManipulationError {
		System.out.println("SORRY on " + state.getGoal());
		return NewCollection.set(NewCollection.pair(state.getGoal(), state.getGoal()));
	}
}
