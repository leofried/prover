package prover.instruction.sentence.other;

import java.util.List;

import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.OperatorDeclerationError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Parser;
import prover.reader.readers.Validator;
import prover.state.base.KnowledgeBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.standard.standards.RealOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.equality.EqualityProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.UniversalProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;

public class OperatorSentence<B extends KnowledgeBase, E extends Entity<E>> extends Sentence<B, Pair<Class<E>, Boolean>> {

	public static <B extends KnowledgeBase> OperatorSentence<B, ?> factory(Lexer lex, Namespace space, Validator valid, boolean transparent) throws SyntaxError {
		if(lex.check(Constants.PREDICATE))     return new OperatorSentence<B, Proposition>(lex, space, valid, NewCollection.pair(Proposition.class, transparent));
		else if(lex.check(Constants.FUNCTION)) return new OperatorSentence<B, Element    >(lex, space, valid, NewCollection.pair(Element    .class, transparent));
		else throw new UnexpectedTokenError(lex);
	}
	
	private Operator<E> operator;
	private Definition<E> definition;
	private boolean local;

	private OperatorSentence(Lexer lex, Namespace space, Validator valid, Pair<Class<E>, Boolean> pair) throws SyntaxError {
		super(lex, space, valid, pair);
	}

	protected void compile(Lexer lex, Namespace space, Validator valid, Pair<Class<E>, Boolean> pair) throws SyntaxError {		
		Class<E> klass = pair.left;
		boolean transparent = pair.right;
		
		Integer strength = null;
		
		if(lex.check(Constants.STRENGTH.left)) {
			strength = lex.nextInteger();
			lex.force(Constants.STRENGTH.right);
			if(klass == Element.class && strength < Parser.BINARY_STRENGTH) throw new OperatorDeclerationError(lex, "Cannot declare nonpositive function strength");
			if(klass == Proposition.class && strength != Parser.PREDICATE_STRENGTH) throw new OperatorDeclerationError(lex, "Cannot declare nonzero predicate strength");
		}

		String name = lex.nextName(true);

		if(strength == null && transparent && lex.check(Constants.FROM)) {
			space.renameOperator(lex, name, klass);
		} else {
			Pair<Namespace, List<List<? extends Operator<?>>>> operators = Parser.readOperators(lex, space, true);
			operator = new RealOperator<E>(name, new PlatonicArguments(operators.right.get(0), operators.right.get(1), operators.right.get(2).size()), klass, strength);

			if(lex.check(Constants.DEFINITIONS.right)) {
				E entity = Parser.readEntity(lex, operators.left, klass);
				definition = new Definition<E>((List<Operator<Proposition>>) operators.right.get(0), (List<Operator<Element>>)operators.right.get(1), (List<Operator<Element>>) operators.right.get(2), entity);
				
				if(transparent) {
					local = false;
				} else {
					if(lex.check(Constants.JUSTIFICATION_SEPARATOR)) {
						TestArguments tests = new TestArguments(operator.getArguments());

						E opr = operator.convert(tests);
						E def = definition.convert(tests);

						Proposition prop;
						if(klass == Proposition.class) {
							prop = Proposition.iff((Proposition) opr, (Proposition) def);
						} else if(klass == Element.class) {
							prop = new EqualityProposition((Element) opr, (Element) def);
						} else {
							throw new AssertionError();
						}

						for(int i = tests.getElements().size() - 1; i >= 0; i--) {
							prop = new UniversalProposition(tests.getElements().get(i), prop);
						}

						Definition<Proposition> theorem = new Definition<Proposition>(new TestArguments(tests.getPredicates(), tests.getFunctions()), prop);
						
						space.addTheorem(lex, lex.nextName(true), theorem, true);
						
						definition = null;
					} else {
						local = true;
					}
				}				
			} else if(transparent) {
				throw new OperatorDeclerationError(lex, "Cannot declare transparent operator without definition");
			}
			
			space.addOperator(lex, operator);
		}
	}

	@Override
	public Proposition run(B base, Validator valid) {
		base.addDefinition(operator, definition, local);
		if(operator != null && operator.getConverterClass() == Element.class && operator.getArguments().equals(PlatonicArguments.EMPTY)) base.notEmptyModel();
		return null;
	}

}
