package prover.instruction.sentence.goal.proof.proofs;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.proof.ProofSentence;
import prover.reader.readers.Lexer;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public class NamedLemmaSentence extends ProofSentence<TheoremBase> {

	public NamedLemmaSentence(Lexer lex, Namespace space) throws SyntaxError {
		super(lex, space, true);
	}

	@Override
	protected Proposition uponResolution(TheoremBase base) {
		return null;
	}

	
}
