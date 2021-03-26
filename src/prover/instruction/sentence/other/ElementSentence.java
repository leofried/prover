package prover.instruction.sentence.other;

import java.util.List;

import prover.error.logic.LogicError;
import prover.error.logic.logics.GoalManipulationError;
import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;

public class ElementSentence extends Sentence<TheoremBase, Object> {
	
	private List<Operator<Element>> elements;
	private Sentence<TheoremBase, ?> sentence;

	public ElementSentence(Lexer lex, Namespace space) throws SyntaxError {
		super(lex, space);
	}
	
	protected void init(Lexer lex, Namespace space, Object obj) throws SyntaxError {
		List<String> names = NewCollection.list();
		do {
			names.add(lex.nextName(true));
		} while (lex.check(Constants.COMMA));

		lex.force("from");
		
		if(lex.check("goal")) sentence = null;
		else sentence = new StatementSentence(lex, space, true);
		
		elements = NewCollection.list();
		for(String name : names) {
			elements.add(space.addElement(lex, name));
		}
	}

	@Override
	public Proposition execute(TheoremBase base) throws LogicError {
		if(sentence == null) {
			for(Operator<Element> element : elements) {
				Proposition goal = base.getGoal().applyElementUniversal(element.convert());
				if(goal == null) throw new GoalManipulationError(location(), "Can only get element from universal goal");
				base.setGoal(goal);
			}
		} else {
			Proposition truth = sentence.execute(base);
			for(Operator<Element> element : elements) {
				truth = truth.applyElementExistential(element.convert());
				if(truth == null) throw new GoalManipulationError(location(), "Can only get element from existential truth");
				base.addTruth(truth);
			}
		}
		return null;
	}

}
