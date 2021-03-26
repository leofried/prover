package prover.instruction.sentence.other;

import java.util.List;
import java.util.Set;

import prover.error.logic.logics.AmbiguousDefinitionError;
import prover.error.logic.logics.CannotReduceError;
import prover.error.logic.logics.GoalManipulationError;
import prover.error.syntax.SyntaxError;
import prover.instruction.justification.Justification;
import prover.instruction.justification.justifications.EmptyTheoremJustification;
import prover.instruction.justification.justifications.FullTheoremJustification;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Parser;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class TruthSentence extends Sentence<TheoremBase, Object> {

	private Proposition prop;
	private Justification just;

	public TruthSentence(Lexer lex, Namespace space) throws SyntaxError {
		super(lex, space);
	}

	protected void init(Lexer lex, Namespace space, Object obj) throws SyntaxError {
		if(!lex.peek().equals(Constants.JUSTIFICATION_SEPARATOR)) prop = Parser.readTruthEntity(lex, space);

		lex.force(Constants.JUSTIFICATION_SEPARATOR);

		if(lex.check("assumption")) just = Justification.ASSUMPTION;
		else if(lex.check("contradiction")) just = Justification.CONTRADICTION;
		else if(lex.check("definition")) just = Justification.DEFINITION;
		else if(lex.check("substitution")) just = Justification.SUBSTITUTION;
		else if(lex.check("truths")) just = Justification.TRUTHS;
		else if(lex.check("sorry")) just = Justification.SORRY;
		else {
			Definition<Proposition> theorem = space.getTheorem(lex, lex.nextName(true));

			if(lex.check(Constants.DECLARATION_SEPARATOR)) {
				List<Definition<Proposition>> predicates = NewCollection.list();
				List<Definition<Element>> functions = NewCollection.list();

				lex.nextBracket(-1, theorem.getArguments().getPredicates().size(), Constants.PREDICATES);
				for(int i=0; i<theorem.getArguments().getPredicates().size(); i++) {
					predicates.add(Parser.readDefinition(lex, space, theorem.getArguments().getPredicates().get(i), true, Proposition.class));
					lex.nextBracket(i, theorem.getArguments().getPredicates().size(), Constants.PREDICATES);
				}


				lex.nextBracket(-1, theorem.getArguments().getFunctions().size(), Constants.FUNCTIONS);
				for(int i=0; i<theorem.getArguments().getFunctions().size(); i++) {
					functions.add(Parser.readDefinition(lex, space, theorem.getArguments().getFunctions().get(i), true, Element.class));
					lex.nextBracket(i, theorem.getArguments().getFunctions().size(), Constants.FUNCTIONS);
				}

				just = new FullTheoremJustification(theorem, predicates, functions);
			} else {
				just = new EmptyTheoremJustification(theorem);
			}
		}
	}

	@Override
	public Proposition execute(TheoremBase base) throws CannotReduceError, GoalManipulationError, AmbiguousDefinitionError {
		if(prop == null) prop = base.getGoal();
		base.addStructureInformation(prop);

		Set<Pair<Proposition, Proposition>> truths = just.getTruths(location(), base, prop);
		if(truths != null) {
			Set<Proposition> props = NewCollection.set(prop);
			for(Proposition truth : base.getTruthsAndEqualities()) {
				props.addAll(NewCollection.nullToEmpty(prop.getDifferenceEquality(truth)));
			}

			boolean reduced = false;
			for(Pair<Proposition, Proposition> truth : truths) {	
				if(truth.left.doesThisProve(base, props)) {
					base.setGoal(truth.right);
					reduced = true;
					break;
				}
			}

			if(!reduced) {
				for(Pair<Proposition, Proposition> truth : truths) {	
					truth.left.doesThisProve(base, props);
				}
				System.out.println(prop);
				throw new CannotReduceError(location());
			}
		}

		base.addTruth(prop);
		return prop;
	}

}
