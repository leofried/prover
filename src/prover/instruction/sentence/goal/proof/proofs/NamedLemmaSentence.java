package prover.instruction.sentence.goal.proof.proofs;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.proof.ProofSentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.KnowledgeBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public class NamedLemmaSentence<B extends KnowledgeBase> extends ProofSentence<B> {

	public NamedLemmaSentence(Lexer lex, Namespace space, Validator valid) throws SyntaxError {
		super(lex, space, valid, true);
	}

	@Override
	protected Proposition uponResolution(B base, boolean onContract) {
		return null;
	}
	
	protected boolean isGlobal() {
		return false;
	}
	
}
