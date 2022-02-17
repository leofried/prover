package prover.instruction.sentence.goal;

import java.util.List;

import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Parser;
import prover.reader.readers.Validator;
import prover.state.base.KnowledgeBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public abstract class GoalSentence<B extends KnowledgeBase> extends Sentence<B, Boolean> {

	private String name;
	private Namespace newSpace;
	private Proposition goal;

	public GoalSentence(Lexer lex, Namespace space, Validator valid, boolean requireName) throws SyntaxError {
		super(lex, space, valid, requireName);
	}
	
	@Override
	protected void compile(Lexer lex, Namespace space, Validator valid, Boolean requireName) throws SyntaxError {
		name = lex.nextName(requireName);
		if(name == null) name = new String();
		
		Pair<Namespace, List<List<? extends Operator<?>>>> pair = Parser.readOperators(lex, space, false);
		newSpace = pair.left;
		
		lex.force(Constants.DECLARATION_SEPARATOR);

		goal = Parser.readEntity(lex, newSpace, Proposition.class);
		if(!name.isEmpty()) space.addTheorem(lex, name, new Definition<Proposition>((List<Operator<Proposition>>) pair.right.get(0), (List<Operator<Element>>) pair.right.get(1), NewCollection.list(), goal), isGlobal());
	}

	protected String getName() {
		return name;
	}

	protected Namespace getNewSpace() {
		return newSpace;
	}

	protected Proposition getGoal() {
		return goal;
	}
	
	protected abstract boolean isGlobal();
}
