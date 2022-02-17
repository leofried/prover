package prover.instruction.sentence.goal.proof;

import java.util.List;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.goal.GoalSentence;
import prover.instruction.sentence.other.StatementSentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.KnowledgeBase;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;

public abstract class ProofSentence<B extends KnowledgeBase> extends GoalSentence<B> {

	private List<StatementSentence> sentences;
	private boolean onContract;

	public ProofSentence(Lexer lex, Namespace space, Validator valid, boolean requireName) throws SyntaxError {
		super(lex, space, valid, requireName);

		sentences = NewCollection.list();

		lex.force(Constants.BLOCKS.left);
		while(true) {
			if(lex.check(Constants.BLOCKS.right)) {
				break;
			} else {
				sentences.add(new StatementSentence(lex, getNewSpace(), valid, 0));
			}
		}
		
		onContract = lex.onContract();
	}

	@Override
	public Proposition run(B base, Validator valid) throws LogicError {		
		if(!onContract || !valid.isValid()) {
			TheoremBase theoremBase = new TheoremBase(base, getName(), getGoal());
	
			for(StatementSentence sentence : sentences) {
				sentence.run(theoremBase, valid);
			}
	
			theoremBase.resolve(location());
		}
		
		return uponResolution(base, onContract && valid.isValid());
	}

	protected abstract Proposition uponResolution(B base, boolean onContract);
}
