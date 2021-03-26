package prover.instruction.justification.justifications;

import java.util.Set;

import prover.error.logic.logics.GoalManipulationError;
import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Pair;

public class ContradictionJustification extends Justification {

	private static ContradictionJustification instance = null;
	
	public static ContradictionJustification getInstance() {
		if(instance == null) instance = new ContradictionJustification();
		return instance;
	}
	
	private ContradictionJustification() {}

	@Override
	public Set<Pair<Proposition, Proposition>> getTruths(String loc, TheoremBase state, Proposition prop) throws GoalManipulationError {
		if(!prop.equals(state.getGoal().not())) throw new GoalManipulationError(loc, "Contradictions must be negations of the goal");
		state.setGoal(Proposition.FALSE);
		return null;
	}
}
