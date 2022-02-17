package prover.reader.readers;

import java.util.List;

import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.NoSuchNameError;
import prover.error.syntax.syntaxes.OperatorDeclerationError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.error.syntax.syntaxes.WrongEntityTypeError;
import prover.error.syntax.syntaxes.WrongNumberOfArgumentsError;
import prover.reader.Reader;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.standard.standards.RealOperator;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.junction.junctions.ConjunctionProposition;
import prover.structure.regular.entity.proposition.binary.junction.junctions.DisjunctionProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.ExistentialProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.UniversalProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;


public abstract class Parser extends Reader {

	public static final int EMPTY_STRENGTH		= Integer.MIN_VALUE;
	public static final int IFF_STRENGTH		= -7;
	public static final int IF_STRENGTH			= -6;
	public static final int OR_STRENGTH			= -5;
	public static final int AND_STRENGTH		= -3;
	public static final int QUANT_STRENGTH		= -1;
	public static final int PREDICATE_STRENGTH	=  0;
	public static final int BINARY_STRENGTH     =  1;
	public static final int UNARY_STRENGTH      =  Integer.MAX_VALUE;

	public static boolean bounce(int newOperator, int oldOperator) {
		if(newOperator < oldOperator) return true;
		else if(newOperator > oldOperator) return false;
		else return newOperator % 2 != 0;
	}

	public static <E extends Entity<E>> E readEntity(Lexer lex, Namespace space, Class<E> klass) throws SyntaxError {	
		return readStrengthedEntity(lex, space, EMPTY_STRENGTH, klass);
	}

	private static <E extends Entity<E>> E readStrengthedEntity(Lexer lex, Namespace space, int strength, Class<E> klass) throws SyntaxError {
		return readEntityContainer(lex, space, strength, false, klass);
	}

	public static Proposition readTruthEntity(Lexer lex, Namespace space) throws SyntaxError {
		space.primeFill();
		return readEntityContainer(lex, space, EMPTY_STRENGTH, true, Proposition.class);
	}

	private static <E extends Entity<E>> E readEntityContainer(Lexer lex, Namespace space, int strength, boolean fillSymbols, Class<E> klass) throws SyntaxError {
		Entity<?> entity =  readEntityComputer(lex, space, strength, fillSymbols);
		try {
			return klass.cast(entity);
		} catch (ClassCastException e) {
			throw new WrongEntityTypeError(lex, entity);
		}
	}

	private static Entity<?> readUnknownEntity(Lexer lex, Namespace space) throws SyntaxError {	
		return readEntityComputer(lex, space, EMPTY_STRENGTH, false);
	}

	private static Entity<?> readEntityComputer(Lexer lex, Namespace space, int strength, boolean isTruthEntity) throws SyntaxError {
		Entity<?> entity = null;

		while(true) {

			if(lex.check(Constants.PARENS.left)) {
				if(entity != null) return lex.undo(entity);
				entity = readUnknownEntity(lex, space);
				lex.force(Constants.PARENS.right);
			}

			else if(lex.check(Constants.IFF)) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || bounce(IFF_STRENGTH, strength)) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, IFF_STRENGTH, Proposition.class);
				entity = Proposition.iff((Proposition) entity, prop);
			}

			else if(lex.check(Constants.IF)) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || bounce(IF_STRENGTH, strength)) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, IF_STRENGTH, Proposition.class);
				entity = Proposition.implies((Proposition) entity, prop);
			}

			else if(lex.check(Constants.OR)) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || bounce(OR_STRENGTH, strength)) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, OR_STRENGTH,  Proposition.class);
				entity = new DisjunctionProposition(((Proposition) entity), prop);
			}

			else if(lex.check(Constants.AND)) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || bounce(AND_STRENGTH, strength)) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, AND_STRENGTH, Proposition.class);
				entity = new ConjunctionProposition((Proposition) entity, prop);
			}

			else if(lex.check(Constants.NOT)) {
				if(entity != null) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, QUANT_STRENGTH, Proposition.class);
				entity = prop.not();
			}

			else if(lex.peek().equals(Constants.FORALL.left)) {
				if(entity != null) return entity;

				Pair<TestOperator<Element>, Pair<Proposition, Proposition>> pair = readQuantifier(lex, space, Constants.FORALL);
				if(pair.right.left == null) entity = new UniversalProposition(pair.left, pair.right.right);
				else entity = new UniversalProposition(pair.left, Proposition.implies(pair.right.left, pair.right.right));
			}

			else if(lex.peek().equals(Constants.EXISTS.left)) {
				if(entity != null) return entity;

				Pair<TestOperator<Element>, Pair<Proposition, Proposition>> pair = readQuantifier(lex, space, Constants.EXISTS);
				if(pair.right.left == null) entity = new ExistentialProposition(pair.left, pair.right.right);
				else entity = new ExistentialProposition(pair.left, new ConjunctionProposition(pair.right.left, pair.right.right));
			}

			else {				
				Operator<?> operator;
				try {
					operator = space.getOperator(lex);
				} catch(NoSuchNameError | UnexpectedTokenError e) {
					if(entity != null) return lex.undo(entity);
					else throw new UnexpectedTokenError(lex.undo()); 
				}

				if(operator.getArguments().equals(new PlatonicArguments(1))) {
					if(entity != null) return lex.undo(entity);
					entity = operator.convert(NewCollection.list(readStrengthedEntity(lex, space, Parser.UNARY_STRENGTH, Element.class)));
				} else if(operator.getStrength() == null){
					if(entity != null) return lex.undo(entity);

					List<Definition<Proposition>> predicates = NewCollection.list();
					List<Definition<Element>> functions = NewCollection.list();
					List<Element> elements = NewCollection.list();

					lex.nextBracket(-1, operator.getArguments().getPredicates().size(), Constants.PREDICATES);
					for(int i=0; i<operator.getArguments().getPredicates().size(); i++) {
						predicates.add(readDefinition(lex, space, operator.getArguments().getPredicates().get(i), Proposition.class));
						lex.nextBracket(i, operator.getArguments().getPredicates().size(), Constants.PREDICATES);
					}


					lex.nextBracket(-1, operator.getArguments().getFunctions().size(), Constants.FUNCTIONS);
					for(int i=0; i<operator.getArguments().getFunctions().size(); i++) {
						functions.add(readDefinition(lex, space, operator.getArguments().getFunctions().get(i), Element.class));
						lex.nextBracket(i, operator.getArguments().getFunctions().size(), Constants.FUNCTIONS);
					}


					lex.nextBracket(-1, operator.getArguments().getElements(), Constants.ELEMENTS);
					for(int i=0; i<operator.getArguments().getElements(); i++) {
						elements.add(readEntity(lex, space, Element.class));
						lex.nextBracket(i, operator.getArguments().getElements(), Constants.ELEMENTS);
					}

					entity = operator.convert(predicates, functions, elements);
				} else {
					boolean filled = false;
					if(entity == null && isTruthEntity) {
						entity = space.getFill().left;
						filled = true;
					}
					if(entity == null) throw new UnexpectedTokenError(lex.undo());
					if(!(entity instanceof Element)) throw new WrongEntityTypeError(lex, entity);

					if(bounce(operator.getStrength(), strength)) return lex.undo(entity);
					
					List<Element> elements = NewCollection.list();
					lex.nextBracket(-1, operator.getArguments().getElements() - 2, Constants.ELEMENTS);
					for(int i=0; i<operator.getArguments().getElements() - 2; i++) {
						elements.add(readEntity(lex, space, Element.class));
						lex.nextBracket(i, operator.getArguments().getElements() - 2, Constants.ELEMENTS);
					}
					
					Element element;
					if(isTruthEntity && lex.peek().equals(Constants.JUSTIFICATION_SEPARATOR) && operator.getConverterClass() == Proposition.class) {
						element = space.getFill().right;
						if(element == null) throw new UnexpectedTokenError(lex);
					} else {
						element = readStrengthedEntity(lex, space, operator.getStrength(), Element.class);

						if(filled) {
							lex.force(Constants.JUSTIFICATION_SEPARATOR);
							lex.undo();
						}
					}

					if(isTruthEntity && lex.peek().equals(Constants.JUSTIFICATION_SEPARATOR)) space.setFill((Element) entity, element);
					entity = operator.convert(NewCollection.list(elements, (Element) entity, element));
				}
			}
		}
	}

	private static Pair<TestOperator<Element>, Pair<Proposition, Proposition>> readQuantifier(Lexer lex, Namespace space, Pair<String, String> parens) throws SyntaxError {
		lex.force(parens.left);
		TestOperator<Element> instance = new TestOperator<Element>(lex.nextName(true));

		Entity<?> bound = null;
		if(!lex.peek().equals(parens.right)) {
			Operator<?> operator = space.getOperator(lex);
			if(operator.getStrength() == null || operator.getStrength() != PREDICATE_STRENGTH) throw new WrongNumberOfArgumentsError(lex, 2);

			bound = operator.convert(NewCollection.list(instance.convert(), readStrengthedEntity(lex, space, operator.getStrength(), Element.class)));
			if(!(bound instanceof Proposition)) throw new WrongEntityTypeError(lex, bound);			
		}

		lex.force(parens.right);

		space.addTestOperator(instance, lex);
		Proposition prop = readStrengthedEntity(lex, space, QUANT_STRENGTH, Proposition.class);
		space.removeTestOperator(instance);

		return NewCollection.pair(instance, NewCollection.pair((Proposition) bound, prop));
	}

	public static <E extends Entity<E>> Definition<E> readDefinition(Lexer lex, Namespace space, PlatonicArguments arguments, Class<E> klass) throws SyntaxError {
		if(arguments.getPredicates().size() + arguments.getFunctions().size() + arguments.getElements() != 0 && !lex.peek().equals(Constants.DEFINITIONS.left)) {
			Operator<?> operator = space.getOperator(lex);

			if(operator.getConverterClass() != klass) throw new OperatorDeclerationError(lex, "Wrong Operator Type");
			if(!arguments.equals(operator.getArguments())) throw new OperatorDeclerationError(lex, "Wrong Operator Arguments");

			return new Definition<E>((Operator<E>) operator);
		}

		List<TestOperator<Proposition>> predicates = NewCollection.list();
		List<TestOperator<Element>> functions = NewCollection.list();
		List<TestOperator<Element>> elements = NewCollection.list();

		if(arguments.getPredicates().size() + arguments.getFunctions().size() + arguments.getElements() != 0 || lex.peek().equals(Constants.DEFINITIONS.left)){
			lex.force(Constants.DEFINITIONS.left);

			lex.nextBracket(-1, arguments.getPredicates().size(), Constants.PREDICATES);
			for(int i=0; i<arguments.getPredicates().size(); i++) {
				TestOperator<Proposition> predicate = new TestOperator<Proposition>(lex.nextName(true), arguments.getPredicates().get(i), Proposition.class);
				predicates.add(predicate);
				space.addTestOperator(predicate, lex);

				lex.nextBracket(i, arguments.getPredicates().size(), Constants.PREDICATES);
			}


			lex.nextBracket(-1, arguments.getFunctions().size(), Constants.FUNCTIONS);
			for(int i=0; i<arguments.getFunctions().size(); i++) {
				TestOperator<Element> function = new TestOperator<Element>(lex.nextName(true), arguments.getFunctions().get(i), Element.class);
				functions.add(function);
				space.addTestOperator(function, lex);

				lex.nextBracket(i, arguments.getFunctions().size(), Constants.FUNCTIONS);
			}


			lex.nextBracket(-1, arguments.getElements(), Constants.ELEMENTS);
			for(int i=0; i<arguments.getElements(); i++) {
				TestOperator<Element> element = new TestOperator<Element>(lex.nextName(true));
				elements.add(element);
				space.addTestOperator(element, lex);

				lex.nextBracket(i, arguments.getElements(), Constants.ELEMENTS);
			}

			lex.force(Constants.DEFINITIONS.right);
		}

		E entity = Parser.readEntity(lex, space, klass);
		space.removeTestOperators(predicates);
		space.removeTestOperators(functions);
		space.removeTestOperators(NewCollection.list(elements));

		return new Definition<E>(new TestArguments(predicates, functions, elements), entity);
	}

	public static Pair<Namespace, List<List<? extends Operator<?>>>> readOperators(Lexer lex, Namespace space, boolean allowElements) throws SyntaxError {
		Namespace newSpace = new Namespace(lex, space);

		List<Operator<Proposition>> predicates = NewCollection.list();
		if(lex.check(Constants.PREDICATES.left)) {
			boolean first = true;
			while(!lex.check(Constants.PREDICATES.right)) {
				if(first) first = false;
				else lex.force(Constants.COMMA);

				String opName = lex.nextName(true);
				PlatonicArguments arguments = readArguments(lex, space);
				Operator<Proposition> operator = new RealOperator<Proposition>(opName, arguments, Proposition.class, null);	
				predicates.add(operator);
				newSpace.addOperator(lex, operator);
			}
		}

		List<Operator<Element>> functions = NewCollection.list();
		if(lex.check(Constants.FUNCTIONS.left)) {
			boolean first = true;
			while(!lex.check(Constants.FUNCTIONS.right)) {
				if(first) first = false;
				else lex.force(Constants.COMMA);

				String opName = lex.nextName(true);
				PlatonicArguments arguments = readArguments(lex, space);
				Operator<Element> operator = new RealOperator<Element>(opName, arguments, Element.class, null);	
				functions.add(operator);
				newSpace.addOperator(lex, operator);
			}
		}

		List<Operator<Element>> elements = NewCollection.list();
		if(allowElements && lex.check(Constants.ELEMENTS.left)) {
			boolean first = true;
			while(!lex.check(Constants.ELEMENTS.right)) {
				if(first) first = false;
				else lex.force(Constants.COMMA);

				String opName = lex.nextName(true);
				Operator<Element> operator = new RealOperator<Element>(opName, PlatonicArguments.EMPTY, Element.class, null);	
				elements.add(operator);
				newSpace.addOperator(lex, operator);
			}
		}

		return NewCollection.pair(newSpace, NewCollection.<List<? extends Operator<?>>>list(predicates, functions, elements));
	}

	private static PlatonicArguments readArguments(Lexer lex, Namespace space) throws UnexpectedTokenError {
		List<PlatonicArguments> predicates = NewCollection.list();
		if(lex.check(Constants.PREDICATES.left)) {
			boolean first = true;
			while(!lex.check(Constants.PREDICATES.right)) {
				if(first) first = false;
				else lex.force(Constants.COMMA);

				predicates.add(readArguments(lex, space));
			}

		}

		List<PlatonicArguments> functions = NewCollection.list();
		if(lex.check(Constants.FUNCTIONS.left)) {
			if(lex.peek().equals(Constants.PREDICATES.left) ||
					lex.peek().equals(Constants.PREDICATES.right) ||
					lex.peek().equals(Constants.FUNCTIONS .left ) ||
					lex.peek().equals(Constants.FUNCTIONS .right) ||
					lex.peek().equals(Constants.ELEMENTS  .left ) ||
					lex.peek().equals(Constants.ELEMENTS  .left )) {

				boolean first = true;
				while(!lex.check(Constants.FUNCTIONS.right)) {
					if(first) first = false;
					else lex.force(Constants.COMMA);

					functions.add(readArguments(lex, space));
				}
			} else {
				lex.undo();
			}
		}

		int elements = 0;
		if(lex.check(Constants.ELEMENTS.left)) {
			elements = lex.nextInteger();
			lex.force(Constants.ELEMENTS.right);
		}

		return new PlatonicArguments(predicates, functions, elements);
	}

}