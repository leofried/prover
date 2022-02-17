package prover.instruction.sentence.goal.axiom;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.GoalSentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public class AxiomSentence extends GoalSentence<FileBase> {

	public AxiomSentence(Lexer lex, Namespace space, Validator valid) throws SyntaxError {
		super(lex, space, valid, true);
	}

	@Override
	public Proposition run(FileBase base, Validator valid) {
		return null;
	}
	
	protected boolean isGlobal() {
		return true;
	}

}
