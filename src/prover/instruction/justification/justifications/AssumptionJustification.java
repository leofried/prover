package prover.instruction.justification.justifications;

import java.util.Set;

import prover.error.logic.logics.GoalManipulationError;
import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Pair;

public class AssumptionJustification extends Justification {

	private static AssumptionJustification instance = null;
	
	public static AssumptionJustification getInstance() {
		if(instance == null) instance = new AssumptionJustification();
		return instance;
	}
	
	private AssumptionJustification() {}
	
	@Override
	public Set<Pair<Proposition, Proposition>> getTruths(String loc, TheoremBase state, Proposition prop) throws GoalManipulationError {
		Set<Pair<Proposition, Proposition>> truths = state.getGoal().getAssumptionSet();
		if(truths == null) throw new GoalManipulationError(loc, "Assumptions can only be made from implications and disjunctions");
		return truths;
	}
}
