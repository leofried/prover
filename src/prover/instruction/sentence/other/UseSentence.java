package prover.instruction.sentence.other;

import prover.error.logic.logics.CannotReduceError;
import prover.error.logic.logics.GoalManipulationError;
import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Parser;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;;

public class UseSentence extends Sentence<TheoremBase, Object> {

	private Element element;
	
	public UseSentence(Lexer lex, Namespace space) throws SyntaxError {
		super(lex, space);
	}
	
	protected void init(Lexer lex, Namespace space, Object obj) throws SyntaxError {
		element = Parser.readEntity(lex, space, Element.class);
	}

	@Override
	public Proposition execute(TheoremBase base) throws CannotReduceError, GoalManipulationError {
		Proposition goal = base.getGoal().applyElementExistential(element);
		if(goal == null) throw new GoalManipulationError(location(), "Can use element on a existential goal");
		base.setGoal(goal);
		return null;
	}

}
