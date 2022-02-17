package prover.instruction.sentence.other;

import java.util.List;
import java.util.Set;

import prover.error.logic.logics.CannotReduceError;
import prover.error.logic.logics.GoalManipulationError;
import prover.error.syntax.SyntaxError;
import prover.instruction.justification.Justification;
import prover.instruction.justification.justifications.EmptyTheoremJustification;
import prover.instruction.justification.justifications.FullTheoremJustification;
import prover.instruction.sentence.Sentence;
import prover.reader.Reader;
import prover.reader.readers.Lexer;
import prover.reader.readers.Parser;
import prover.reader.readers.Validator;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.Logger;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class TruthSentence extends Sentence<TheoremBase, Object> {

	private Proposition prop;
	private Justification just;

	public TruthSentence(Lexer lex, Namespace space, Validator valid) throws SyntaxError {
		super(lex, space, valid);
	}

	@Override
	protected void compile(Lexer lex, Namespace space, Validator valid, Object obj) throws SyntaxError {
		if(!lex.peek().equals(Constants.JUSTIFICATION_SEPARATOR)) prop = Parser.readTruthEntity(lex, space);

		lex.force(Constants.JUSTIFICATION_SEPARATOR);

		if(lex.check(Constants.ASSUMPTION))        just = Justification.ASSUMPTION;
		else if(lex.check(Constants.SUBSTITUTION)) just = Justification.SUBSTITUTION;
		else if(lex.check(Constants.TRUTHS))       just = Justification.TRUTHS;
		else if(lex.check(Constants.SORRY))        just = Justification.SORRY;
		else {
			String theoremName = lex.nextName(true);			
			Definition<Proposition> theorem = space.getTheorem(lex, theoremName);
			
			valid.useTheorem(theoremName, theorem);

			if(lex.check(Constants.SUBSTITUTION_SEPARATOR)) {
				List<Definition<Proposition>> predicates = NewCollection.list();
				List<Definition<Element>> functions = NewCollection.list();

				lex.nextBracket(-1, theorem.getArguments().getPredicates().size(), Constants.PREDICATES);
				for(int i=0; i<theorem.getArguments().getPredicates().size(); i++) {
					predicates.add(Parser.readDefinition(lex, space, theorem.getArguments().getPredicates().get(i), Proposition.class));
					lex.nextBracket(i, theorem.getArguments().getPredicates().size(), Constants.PREDICATES);
				}


				lex.nextBracket(-1, theorem.getArguments().getFunctions().size(), Constants.FUNCTIONS);
				for(int i=0; i<theorem.getArguments().getFunctions().size(); i++) {
					functions.add(Parser.readDefinition(lex, space, theorem.getArguments().getFunctions().get(i), Element.class));
					lex.nextBracket(i, theorem.getArguments().getFunctions().size(), Constants.FUNCTIONS);
				}

				just = new FullTheoremJustification(theorem, predicates, functions);
			} else {
				just = new EmptyTheoremJustification(theorem);
			}
		}
	}

	
	@Override
	public Proposition run(TheoremBase base, Validator valid) throws CannotReduceError, GoalManipulationError {		
		Proposition adjProp = base.addEntity(prop);
		if(Reader.DEBUG) Logger.log(location());

		List<Pair<Proposition, Pair<Proposition, Integer>>> truths = just.getTruths(location(), base, adjProp);
		
		if(truths != null) {
			Set<Proposition> props = null;
			
			for(Proposition subProp : adjProp.deconstruct()) {
				Set<Proposition> subProps = NewCollection.set();
				for(Proposition truth : base.getTruthsAndEqualities()) {
					subProps.addAll(NewCollection.nullToEmpty(subProp.getDifferenceEquality(truth)));
				}
				props = NewCollection.intersect(props, subProps);
			}
			props.add(adjProp);

			
			boolean reduced = false;
			for(Pair<Proposition, Pair<Proposition, Integer>> truth : truths) {
				if(truth.left.doesThisProve(base, props)) {
					base.setGoal(truth.right);
					reduced = true;
					break;
				}
			}

			if(!reduced) {
				if(Reader.DEBUG) {
					for(Pair<Proposition, Pair<Proposition, Integer>> truth : truths) {	
						truth.left.doesThisProve(base, props);
						Logger.log(truth);
					}
					Logger.log(base.getTruths());
					Logger.log(props);
					Logger.log(adjProp);
				}
				throw new CannotReduceError(location());
			}
		}

		base.addTruth(adjProp);
		return adjProp;
	}

}
