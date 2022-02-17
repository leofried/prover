package prover.instruction.sentence.other;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.instruction.sentence.Sentence;
import prover.instruction.sentence.goal.proof.proofs.NamedLemmaSentence;
import prover.instruction.sentence.goal.proof.proofs.UnnamedLemmaSentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;

public class StatementSentence extends Sentence<TheoremBase, Integer> {

	Sentence<TheoremBase, ?> sentence;

	public StatementSentence(Lexer lex, Namespace space, Validator valid, int restriction) throws SyntaxError {
		super(lex, space, valid, restriction);
	}
	
	protected void compile(Lexer lex, Namespace space, Validator valid, Integer restriction) throws SyntaxError {		
		if(lex.check(Constants.LEMMA)) {
			     if(lex.peek().equals(Constants.DECLARATION_SEPARATOR))		sentence = new UnnamedLemmaSentence(lex, space, valid);
			else if(restriction <= 0)                                   	sentence = new NamedLemmaSentence<TheoremBase>(lex, space, valid);
			else                                                        	throw new UnexpectedTokenError(lex);
		}
		else if(restriction <= 1 && lex.check(Constants.FOR))		   		sentence = new LoopSentence(lex, space, valid);
		else if(restriction <= 0 && lex.check(Constants.ELEMENT))			sentence = new ElementSentence(lex, space, valid);
		else if(restriction <= 0 && (lex.peek().equals(Constants.PREDICATE)
								 || lex.peek().equals(Constants.FUNCTION)))	sentence = OperatorSentence.factory(lex, space, valid, true);
		else {
			                                                            	sentence = new TruthSentence(lex, space, valid);
			                                                            	return;
		}
		
		space.clearFill();
	}

	@Override
	public Proposition run(TheoremBase base, Validator valid) throws LogicError {
		return sentence.run(base, valid);
	}

}
