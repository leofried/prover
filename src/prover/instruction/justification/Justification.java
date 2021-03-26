package prover.instruction.justification;

import java.util.Set;

import prover.error.logic.logics.AmbiguousDefinitionError;
import prover.error.logic.logics.GoalManipulationError;
import prover.instruction.Instruction;
import prover.instruction.justification.justifications.AssumptionJustification;
import prover.instruction.justification.justifications.ContradictionJustification;
import prover.instruction.justification.justifications.DefinitionJustification;
import prover.instruction.justification.justifications.SorryJustification;
import prover.instruction.justification.justifications.SubstitutionJustification;
import prover.instruction.justification.justifications.TruthsJustification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Pair;

public abstract class Justification extends Instruction {

	public static final Justification ASSUMPTION    = AssumptionJustification   .getInstance();
	public static final Justification CONTRADICTION = ContradictionJustification.getInstance();
	public static final Justification DEFINITION    = DefinitionJustification   .getInstance();
	public static final Justification SUBSTITUTION  = SubstitutionJustification .getInstance();
	public static final Justification TRUTHS        = TruthsJustification       .getInstance();
	public static final Justification SORRY         = SorryJustification        .getInstance();
	
	public abstract Set<Pair<Proposition, Proposition>> getTruths(String loc, TheoremBase state, Proposition prop) throws GoalManipulationError, AmbiguousDefinitionError;
}
