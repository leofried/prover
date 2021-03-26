package prover.instruction.sentence.goal.proof.proofs;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.proof.ProofSentence;
import prover.reader.readers.Lexer;
import prover.state.base.KnowledgeBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public class TheoremSentence<B extends KnowledgeBase> extends ProofSentence<B> {

	public TheoremSentence(Lexer lex, Namespace space) throws SyntaxError {
		super(lex, space, false);
	}

	@Override
	protected Proposition uponResolution(B base) {
		if(!getName().isEmpty()) {
			System.out.println("Proof of " + getName() + " is valid!");
		} else {
			System.out.println("Proof is valid!");
		}
		return null;
	}
	
}
