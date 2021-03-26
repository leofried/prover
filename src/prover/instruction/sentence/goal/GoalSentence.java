package prover.instruction.sentence.goal;

import java.util.List;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Parser;
import prover.state.base.KnowledgeBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.standard.real.RealOperator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.PlatonicArguments;

public abstract class GoalSentence<B extends KnowledgeBase> extends Sentence<B, Boolean> {

	private String name;
	private Namespace newSpace;
	private Proposition goal;

	public GoalSentence(Lexer lex, Namespace space, boolean requireName) throws SyntaxError {
		super(lex, space, requireName);
	}
	
	@Override
	protected void init(Lexer lex, Namespace space, Boolean requireName) throws SyntaxError {
		name = lex.nextName(requireName);
		if(name == null) name = "";
		
		newSpace = new Namespace(lex, space);

		List<Operator<Proposition>> predicates = NewCollection.list();
		if(lex.check(Constants.PREDICATES.left)) {
			boolean first = true;
			while(!lex.check(Constants.PREDICATES.right)) {
				if(first) first = false;
				else lex.force(Constants.COMMA);

				String opName = lex.nextName(true);
				PlatonicArguments arguments = Parser.readArguments(lex, space);
				Operator<Proposition> operator = new RealOperator<Proposition>(opName, arguments, Proposition.class);	
				predicates.add(operator);
				newSpace.addOperator(lex, operator, null, true);
			}
		}

		List<Operator<Element>> functions = NewCollection.list();
		if(lex.check(Constants.FUNCTIONS.left)) {
			boolean first = true;
			while(!lex.check(Constants.FUNCTIONS.right)) {
				if(first) first = false;
				else lex.force(Constants.COMMA);

				String opName = lex.nextName(true);
				PlatonicArguments arguments = Parser.readArguments(lex, space);
				Operator<Element> operator = new RealOperator<Element>(opName, arguments, Element.class);	
				functions.add(operator);
				newSpace.addOperator(lex, operator, null, true);
			}
		}

		lex.force(Constants.DECLARATION_SEPARATOR);

		goal = Parser.readEntity(lex, newSpace, Proposition.class);
		
		if(!name.isEmpty()) space.addTheorem(lex, name, new Definition<Proposition>(predicates, functions, goal), true);
	}
	
	public String getName() {
		return name;
	}

	public Namespace getNewSpace() {
		return newSpace;
	}

	public Proposition getGoal() {
		return goal;
	}
	
	@Override
	public final Proposition execute(B base) throws LogicError {
		base.addStructureInformation(getGoal());
		return executeHelper(base);
	}

	protected abstract Proposition executeHelper(B base) throws LogicError;
}
