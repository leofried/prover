package prover.instruction.sentence.other;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.instruction.sentence.Sentence;
import prover.instruction.sentence.goal.proof.proofs.NamedLemmaSentence;
import prover.instruction.sentence.goal.proof.proofs.UnnamedLemmaSentence;
import prover.reader.Reader;
import prover.reader.readers.Lexer;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;

public class StatementSentence extends Sentence<TheoremBase, Boolean> {

	Sentence<TheoremBase, ?> sentence;

	public StatementSentence(Lexer lex, Namespace space, boolean requireTruth) throws SyntaxError {
		super(lex, space, requireTruth);
	}
	
	protected void init(Lexer lex, Namespace space, Boolean requireTruth) throws SyntaxError {		
		if(lex.check("lemma")) {
			if(lex.peek().equals(Constants.DECLARATION_SEPARATOR))					sentence = new UnnamedLemmaSentence(lex, space);
			else if(requireTruth)													throw new UnexpectedTokenError(lex);
			else 																	sentence = new NamedLemmaSentence(lex, space);
		}
		else if(!requireTruth && lex.check("use")) 									sentence = new UseSentence(lex, space);
		else if(!requireTruth && lex.check("predicate")) 							sentence = new OperatorSentence<TheoremBase, Proposition>(lex, space, Proposition.class);
		else if(!requireTruth && lex.check("function")) 							sentence = new OperatorSentence<TheoremBase, Element>(lex, space, Element.class);
		else if(!requireTruth && lex.check("element"))								sentence = new ElementSentence(lex, space);
		else {
																					sentence = new TruthSentence(lex, space);
																					return;
		}
		
		space.clearFill();
	}

	@Override
	public Proposition execute(TheoremBase base) throws LogicError {
		if(Reader.DEBUG) System.out.println(location());
		return sentence.execute(base);
	}

}
