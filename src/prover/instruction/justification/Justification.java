package prover.instruction.justification;

import java.util.List;

import prover.error.logic.logics.GoalManipulationError;
import prover.instruction.Instruction;
import prover.instruction.justification.justifications.AssumptionJustification;
import prover.instruction.justification.justifications.SorryJustification;
import prover.instruction.justification.justifications.SubstitutionJustification;
import prover.instruction.justification.justifications.TruthsJustification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Pair;

public abstract class Justification extends Instruction {

	public static final Justification ASSUMPTION    = new AssumptionJustification();
	public static final Justification SUBSTITUTION  = new SubstitutionJustification();
	public static final Justification TRUTHS        = new TruthsJustification();
	public static final Justification SORRY         = new SorryJustification();
	
	public abstract List<Pair<Proposition, Pair<Proposition, Integer>>> getTruths(String loc, TheoremBase state, Proposition prop) throws GoalManipulationError;
}
