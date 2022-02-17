package prover.instruction.sentence.goal.proof.proofs;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.proof.ProofSentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Logger;

public class TheoremSentence extends ProofSentence<FileBase> {

	public TheoremSentence(Lexer lex, Namespace space, Validator valid) throws SyntaxError {
		super(lex, space, valid, false);
	}

	@Override
	protected Proposition uponResolution(FileBase base, boolean onContract) {
		if(!getName().isEmpty()) {
			Logger.log("Proof of " + getName() + " is valid!");
		} else {
			Logger.log("Proof is valid!");
		}
		return null;
	}

	protected boolean isGlobal() {
		return true;
	}
}
