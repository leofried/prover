package prover.reader.readers;

import java.util.List;

import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.NoSuchNameError;
import prover.error.syntax.syntaxes.OperatorDeclerationError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.error.syntax.syntaxes.WrongEntityTypeError;
import prover.reader.Reader;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.converter.operator.standard.test.element.TestElement;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.flippable.junction.junctions.ConjunctionProposition;
import prover.structure.regular.entity.proposition.binary.flippable.junction.junctions.DisjunctionProposition;
import prover.structure.regular.entity.proposition.binary.implication.ImplicationProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.ExistentialProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.UniversalProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;


public abstract class Parser extends Reader {

	public static final int EMPTY_STRENGTH		= -6;
	public static final int IFF_STRENGTH		= -5;
	public static final int FI_STRENGTH			= -4;
	public static final int IF_STRENGTH			= -3;
	public static final int AND_STRENGTH		= -2;
	public static final int QUANT_STRENGTH		= -1;
	public static final int PREDICATE_STRENGTH	=  0;
	public static final int BINARY_STRENGTH     =  1;
	public static final int UNARY_STRENGTH      =  Integer.MAX_VALUE;

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

	private static Entity<?> readEntityComputer(Lexer lex, Namespace space, int strength, boolean fillSymbols) throws SyntaxError {
		Entity<?> entity = null;

		while(true) {

			if(lex.check(Constants.PARENS.left)) {
				if(entity != null) return lex.undo(entity);
				entity = readUnknownEntity(lex, space);
				lex.force(Constants.PARENS.right);
			}

			else if(lex.check("<->")) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || IFF_STRENGTH <= strength) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, IFF_STRENGTH, Proposition.class);
				entity = Proposition.iff((Proposition) entity, prop);
			}

			else if(lex.check("<-")) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || FI_STRENGTH <= strength) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, FI_STRENGTH, Proposition.class);
				entity = new ImplicationProposition(prop, (Proposition) entity);
			}
				
			else if(lex.check("->")) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || IF_STRENGTH < strength) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, IF_STRENGTH, Proposition.class);
				entity = new ImplicationProposition((Proposition) entity, prop);
			}

			else if(lex.check("AND")) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || AND_STRENGTH <= strength) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, AND_STRENGTH, Proposition.class);
				entity = new ConjunctionProposition((Proposition) entity, prop);
			}

			else if(lex.check("OR")) {
				if(entity == null) throw new UnexpectedTokenError(lex.undo());
				if(!(entity instanceof Proposition) || AND_STRENGTH <= strength) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, AND_STRENGTH,  Proposition.class);
				entity = new DisjunctionProposition((Proposition) entity, prop);
			}

			else if(lex.check("NOT")) {
				if(entity != null) return lex.undo(entity);

				Proposition prop = readStrengthedEntity(lex, space, QUANT_STRENGTH, Proposition.class);
				entity = prop.not();
			}

			else if(lex.peek().equals(Constants.FORALL.left)) {
				if(entity != null) return entity;

				Pair<TestElement, Proposition> pair = readQuantifier(lex, space, Constants.FORALL);
				entity = new UniversalProposition(pair.left, pair.right);
			}

			else if(lex.peek().equals(Constants.EXISTS.left)) {
				if(entity != null) return entity;

				Pair<TestElement, Proposition> pair = readQuantifier(lex, space, Constants.EXISTS);
				entity = new ExistentialProposition(pair.left, pair.right);
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
					entity = space.sugar(operator).convert(NewCollection.list(readStrengthedEntity(lex, space, Parser.UNARY_STRENGTH, Element.class)));
				} else if(operator.getStrength() == null){
					if(entity != null) return lex.undo(entity);

					List<Definition<Proposition>> predicates = NewCollection.list();
					List<Definition<Element>> functions = NewCollection.list();
					List<Element> elements = NewCollection.list();

					lex.nextBracket(-1, operator.getArguments().getPredicates().size(), Constants.PREDICATES);
					for(int i=0; i<operator.getArguments().getPredicates().size(); i++) {
						Definition<Proposition> definition = readDefinition(lex, space, operator.getArguments().getPredicates().get(i), true, Proposition.class);
						predicates.add(definition);
						lex.nextBracket(i, operator.getArguments().getPredicates().size(), Constants.PREDICATES);
					}


					lex.nextBracket(-1, operator.getArguments().getFunctions().size(), Constants.FUNCTIONS);
					for(int i=0; i<operator.getArguments().getFunctions().size(); i++) {
						Definition<Element> definition = readDefinition(lex, space, operator.getArguments().getFunctions().get(i), true, Element.class);
						functions.add(definition);
						lex.nextBracket(i, operator.getArguments().getFunctions().size(), Constants.FUNCTIONS);
					}


					lex.nextBracket(-1, operator.getArguments().getElements(), Constants.ELEMENTS);
					for(int i=0; i<operator.getArguments().getElements(); i++) {
						Element element = readEntity(lex, space, Element.class);
						elements.add(element);
						lex.nextBracket(i, operator.getArguments().getElements(), Constants.ELEMENTS);
					}

					entity = space.sugar(operator).convert(predicates, functions, elements);
				} else {
					boolean filled = false;
					if(entity == null && fillSymbols) {
						entity = space.getFill().left;
						filled = true;
					}
					if(entity == null) throw new UnexpectedTokenError(lex.undo());
					if(!(entity instanceof Element)) throw new WrongEntityTypeError(lex, entity);

					if(operator.getStrength() > strength) {
						Element element;
						if(fillSymbols && lex.peek().equals(Constants.JUSTIFICATION_SEPARATOR) && operator.getConverterClass() == Proposition.class) {
							element = space.getFill().right;
							if(element == null) throw new UnexpectedTokenError(lex);
						} else {
							element = readStrengthedEntity(lex, space, operator.getStrength(), Element.class);

							if(filled) {
								lex.force(Constants.JUSTIFICATION_SEPARATOR);
								lex.undo();
							}
						}
						if(fillSymbols && lex.peek().equals(Constants.JUSTIFICATION_SEPARATOR)) space.setFill((Element) entity, element);
						entity = space.sugar(operator).convert(NewCollection.list((Element) entity, element));
					}else {
						return lex.undo(entity);
					}
				}
			}
		}
	}

	private static Pair<TestElement, Proposition> readQuantifier(Lexer lex, Namespace space, Pair<String, String> parens) throws SyntaxError {
		lex.force(parens.left);
		TestElement instance = new TestElement(lex.nextName(true));
		lex.force(parens.right);

		space.addTestOperator(instance, lex);
		Proposition prop = readStrengthedEntity(lex, space, QUANT_STRENGTH, Proposition.class);
		space.removeTestOperator(instance);

		return NewCollection.pair(instance, prop);
	}

	public static <E extends Entity<E>> Definition<E> readDefinition(Lexer lex, Namespace space, PlatonicArguments arguments, boolean openingColon, Class<E> klass) throws SyntaxError {
		if(openingColon && (arguments.getPredicates().size() + arguments.getFunctions().size() + arguments.getElements() != 0) && !lex.peek().equals(Constants.DECLARATION_SEPARATOR)) {
			Operator<?> operator = space.getOperator(lex);

			if(operator.getConverterClass() != klass) throw new OperatorDeclerationError(lex, "Wrong Operator Type");
			if(!arguments.equals(operator.getArguments())) throw new OperatorDeclerationError(lex, "Wrong Operator Arguments");

			return space.sugar((Operator<E>) operator);
		}

		List<TestOperator<Proposition>> predicates = NewCollection.list();
		List<TestOperator<Element>> functions = NewCollection.list();
		List<TestElement> elements = NewCollection.list();

		if(!openingColon || arguments.getPredicates().size() + arguments.getFunctions().size() + arguments.getElements() != 0 || lex.peek().equals(Constants.DECLARATION_SEPARATOR)){
			if(openingColon) lex.force(Constants.DECLARATION_SEPARATOR);

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
				TestElement element = new TestElement(lex.nextName(true));
				elements.add(element);
				space.addTestOperator(element, lex);

				lex.nextBracket(i, arguments.getElements(), Constants.ELEMENTS);
			}

			lex.force(Constants.DECLARATION_SEPARATOR);
		}

		E entity = Parser.readEntity(lex, space, klass);
		space.removeTestOperators(predicates);
		space.removeTestOperators(functions);
		space.removeTestOperators(NewCollection.list(elements));

		return new Definition<E>(new TestArguments(predicates, functions, elements), entity);
	}

	public static PlatonicArguments readArguments(Lexer lex, Namespace space) throws UnexpectedTokenError {
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