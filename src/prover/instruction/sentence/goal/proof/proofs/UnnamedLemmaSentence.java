package prover.instruction.sentence.goal.proof.proofs;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.proof.ProofSentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public class UnnamedLemmaSentence extends ProofSentence<TheoremBase> {

	public UnnamedLemmaSentence(Lexer lex, Namespace space, Validator valid) throws SyntaxError {
		super(lex, space, valid, false);
	}

	@Override
	protected Proposition uponResolution(TheoremBase base, boolean onContract) {
		base.addTruth(base.addEntity(getGoal()));
		return getGoal();
	}
	
	protected boolean isGlobal() {
		return false;
	}

}
