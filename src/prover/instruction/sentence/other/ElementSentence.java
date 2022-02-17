package prover.instruction.sentence.other;

import java.util.List;

import prover.error.logic.LogicError;
import prover.error.logic.logics.GoalManipulationError;
import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
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

	public ElementSentence(Lexer lex, Namespace space, Validator valid) throws SyntaxError {
		super(lex, space, valid);
	}
	
	protected void compile(Lexer lex, Namespace space, Validator valid, Object obj) throws SyntaxError {
		List<String> names = NewCollection.list();
		do {
			names.add(lex.nextName(true));
		} while (lex.check(Constants.COMMA));

		lex.force(Constants.FROM);
		
		if(lex.check(Constants.GOAL)) sentence = null;
		else sentence = new StatementSentence(lex, space, valid, 2);
		
		elements = NewCollection.list();
		for(String name : names) {
			elements.add(space.addElement(lex, name));
		}
	}

	@Override
	public Proposition run(TheoremBase base, Validator valid) throws LogicError {
		if(fromGoal()) {
			for(Operator<Element> element : getElements()) {
				Proposition goal = base.getGoal().applyElementUniversal(element.convert());
				if(goal == null) throw new GoalManipulationError(location(), "Can only get element from universal goal");
				base.setGoal(goal);
			}
		} else {
			Proposition truth = sentence.run(base, valid);
			for(Operator<Element> element : getElements()) {
				truth = truth.applyElementExistential(element.convert());
				if(truth == null) throw new GoalManipulationError(location(), "Can only get element from existential truth");
				base.addTruth(truth);
			}
		}
		
		for(Operator<Element> element : elements) {
			base.addEntity(element.convert());
		}
		
		base.notEmptyModel();
		
		return null;
	}
	
	public boolean fromGoal() {
		return sentence == null;
	}
	

	public List<Operator<Element>> getElements() {
		return NewCollection.list(elements);
	}

}
