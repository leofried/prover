package prover.instruction.sentence.goal.axiom;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.GoalSentence;
import prover.reader.readers.Lexer;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public class AxiomSentence extends GoalSentence<FileBase> {

	public AxiomSentence(Lexer lex, Namespace space) throws SyntaxError {
		super(lex, space, true);
	}

	@Override
	public Proposition executeHelper(FileBase base) {
		return null;
	}

}
