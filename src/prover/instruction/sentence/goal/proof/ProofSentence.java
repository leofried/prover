package prover.instruction.sentence.goal.proof;

import java.util.List;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.GoalSentence;
import prover.instruction.sentence.other.StatementSentence;
import prover.reader.readers.Lexer;
import prover.state.base.KnowledgeBase;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;

public abstract class ProofSentence<B extends KnowledgeBase> extends GoalSentence<B> {

	private List<StatementSentence> sentences;

	public ProofSentence(Lexer lex, Namespace space, boolean requireName) throws SyntaxError {
		super(lex, space, requireName);

		sentences = NewCollection.list();

		lex.force(Constants.BLOCKS.left);
		while(true) {
			if(lex.check(Constants.BLOCKS.right)) {
				break;
			} else {
				sentences.add(new StatementSentence(lex, getNewSpace(), false));
			}
		}
	}

	@Override
	public Proposition executeHelper(B base) throws LogicError {
		TheoremBase theoremBase = new TheoremBase(base, getName(), getGoal());

		for(StatementSentence sentence : sentences) {
			sentence.execute(theoremBase);
		}

		theoremBase.resolve(location());
		
		return uponResolution(base);
	}

	protected abstract Proposition uponResolution(B base);
}
