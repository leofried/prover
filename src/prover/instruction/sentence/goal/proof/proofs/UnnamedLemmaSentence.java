package prover.instruction.sentence.goal.proof.proofs;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.proof.ProofSentence;
import prover.reader.readers.Lexer;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public class UnnamedLemmaSentence extends ProofSentence<TheoremBase> {

	public UnnamedLemmaSentence(Lexer lex, Namespace space) throws SyntaxError {
		super(lex, space, false);
	}

	@Override
	protected Proposition uponResolution(TheoremBase base) {
		base.addTruth(getGoal());
		return getGoal();
	}

}
