package prover.instruction.sentence.other;

import java.util.List;

import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.OperatorDeclerationError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Parser;
import prover.state.base.KnowledgeBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.standard.real.RealOperator;
import prover.structure.regular.converter.operator.standard.test.element.TestElement;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.flippable.equality.EqualityProposition;
import prover.structure.regular.entity.proposition.binary.flippable.junction.junctions.ConjunctionProposition;
import prover.structure.regular.entity.proposition.binary.implication.ImplicationProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.ExistentialProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.UniversalProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;

public class OperatorSentence<B extends KnowledgeBase, E extends Entity<E>> extends Sentence<B, Class<E>> {

	private Operator<E> operator;
	private Definition<Proposition> justification;
	private boolean useJustification;

	public OperatorSentence(Lexer lex, Namespace space, Class<E> klass) throws SyntaxError {
		super(lex, space, klass);
	}
	
	protected void init(Lexer lex, Namespace space, Class<E> klass) throws SyntaxError {
		String name = lex.nextName(true);
		PlatonicArguments arguments = Parser.readArguments(lex, space);
		
		operator = new RealOperator<E>(name, arguments, klass);
		justification = null;
		
		boolean strengthed = false;
		Definition<E> sugar = null;
		String theorem = null;

		if(lex.check(Constants.BLOCKS.left)) {
			while(true) {
				if(lex.check(Constants.BLOCKS.right)) {
					break;
				} else if(lex.check("strength")) {
					lex.force(Constants.DECLARATION_SEPARATOR);
					if(strengthed) throw new OperatorDeclerationError(lex, "Cannot declare strength twice");
					if(!arguments.equals(new PlatonicArguments(2))) throw new OperatorDeclerationError(lex, "Cannot declare strength unless the operator is binary");
					
					int strength = lex.nextInteger();
					strengthed = true;
					operator.setStrength(strength);
					
					if(klass == Element.class && strength < Parser.BINARY_STRENGTH) throw new OperatorDeclerationError(lex, "Cannot declare nonpositive function strength");
					if(klass == Proposition.class && strength != Parser.PREDICATE_STRENGTH) throw new OperatorDeclerationError(lex, "Cannot declare nonzero predicate strength");
				
					
				} else if(lex.check("sugar")) {
					if(justification != null || sugar != null) throw new OperatorDeclerationError(lex, "Cannot declare definition twice");
					sugar = Parser.readDefinition(lex, space, arguments, false, klass);

				} else if(lex.check("definition")) {
					if(justification != null || sugar != null) throw new OperatorDeclerationError(lex, "Cannot declare definition twice");
					Definition<E> definition = Parser.readDefinition(lex, space, arguments, false, klass);
					
					TestArguments tests = new TestArguments(arguments);

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

					for(TestElement element : tests.getElements()) {
						prop = new UniversalProposition(element, prop);
					}

					justification = new Definition<Proposition>(new TestArguments(tests.getPredicates(), tests.getFunctions()), prop);
				} else if(lex.check("derivation")) {
					lex.force(Constants.DECLARATION_SEPARATOR);
					if(justification != null || sugar != null) throw new OperatorDeclerationError(lex, "Cannot declare definition twice");
					if(klass != Element.class) throw new OperatorDeclerationError(lex, "Cannot declare derivation for predicate"); 

					Definition<Proposition> derivedFrom = space.getTheorem(lex, lex.nextName(true));

					TestArguments tests = new TestArguments(derivedFrom.getArguments());

					List<TestElement> testElements = NewCollection.list();
					Proposition prop = derivedFrom.convert(tests);
					while(prop instanceof UniversalProposition) {
						TestElement test = new TestElement();
						testElements.add(test);
						prop = prop.applyElementUniversal(test.convert());
					}
					if(!(prop instanceof ExistentialProposition)) throw new OperatorDeclerationError(lex, "Cannot declare derivation without existential proposition"); 

					if(!arguments.equals(new PlatonicArguments(NewCollection.list(tests.getPredicates()),
							NewCollection.list(tests.getFunctions()),
							NewCollection.list(testElements)))) throw new OperatorDeclerationError(lex, "Cannot delcare derivation from a theorem without matching arguments");

					TestElement first = new TestElement();
					prop = prop.applyElementExistential(first.convert());
					if(!(prop instanceof ConjunctionProposition)) throw new OperatorDeclerationError(lex, "Cannot declare derivation without conjunction proposition");

					Proposition firstPredicate = ((ConjunctionProposition) prop).getLeft();
					prop = ((ConjunctionProposition) prop).getRight();
					if(!(prop instanceof UniversalProposition)) throw new OperatorDeclerationError(lex, "Cannot declare derivation without universal proposition");

					TestElement second = new TestElement();
					prop = prop.applyElementUniversal(second.convert());
					if(!(prop instanceof ImplicationProposition)) throw new OperatorDeclerationError(lex, "Cannot declare derivation without implication proposition");

					Proposition secondPredicate = ((ImplicationProposition) prop).getLeft();
					if(secondPredicate.getContainedTestElements().contains(first)) throw new OperatorDeclerationError(lex, "Cannot declare derivation with a refrence existence element in uniquenes predicate");
					if(!firstPredicate.equals(secondPredicate, NewCollection.map(first, second))) throw new OperatorDeclerationError(lex, "Cannot declare derivation without the same existence and uniqueness predicate");

					prop = ((ImplicationProposition) prop).getRight();
					if(!prop.equals(new EqualityProposition(first.convert(), second.convert()))) throw new OperatorDeclerationError(lex, "Cannot declare derivation without uniqueness predicate implying equality");

					prop = firstPredicate;
					for(TestElement test : testElements) {
						prop = new UniversalProposition(test, prop);
					}

					justification = new Definition<Proposition>(tests, prop).substituteOperators(new OperatorDefinitionMap(first, new Definition<Element>(((Operator<Element>) operator).convert(tests))));
				} else if(lex.check("theorem")) {
					lex.force(Constants.DECLARATION_SEPARATOR);					
					if(justification == null) throw new OperatorDeclerationError(lex, "Cannot declare theorem without definition or derivation.");
					theorem = lex.nextName(true);
				} else {
					throw new UnexpectedTokenError(lex);
				}
			}
		}

		space.addOperator(lex, operator, sugar, true);

		if(justification != null) {
			useJustification = true;
		}
		
		if(theorem != null) {
			space.addTheorem(lex, theorem, justification, true);
			useJustification = false;
		}
	}
	
	@Override
	public Proposition execute(B base) {
		base.addStructureInformation(operator);
		if(useJustification) base.addJustification(justification);
		return null;
	}

}
