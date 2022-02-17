package prover.instruction.justification.justifications;

import java.util.List;

import prover.error.logic.logics.GoalManipulationError;
import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Logger;
import prover.utility.utilities.Pair;

public class SorryJustification extends Justification {

	public SorryJustification() {}
	
	@Override
	public List<Pair<Proposition, Pair<Proposition, Integer>>> getTruths(String loc, TheoremBase state, Proposition prop) throws GoalManipulationError {
		Logger.log("SORRY on " + prop);
		return null;
	}
}
