package prover.state.space;

import java.util.List;
import java.util.Map;
import java.util.Set;

import prover.error.syntax.SyntaxError;
import prover.error.syntax.SyntaxError.StringType;
import prover.error.syntax.syntaxes.DuplicateNameError;
import prover.error.syntax.syntaxes.NoSuchNameError;
import prover.error.syntax.syntaxes.OperatorDeclerationError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.reader.readers.Lexer;
import prover.state.State;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.other.DisqualityOperator;
import prover.structure.regular.converter.operator.other.EqualityOperator;
import prover.structure.regular.converter.operator.other.FalseOperator;
import prover.structure.regular.converter.operator.other.TrueOperator;
import prover.structure.regular.converter.operator.standard.standards.RealOperator;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;

public class Namespace extends State {
	
	private Set<TestOperator<?>> tests;
	private Set<Operator<?>> operators;
	private Map<String, Operator<?>> renames;
	private Map<String, Pair<Definition<Proposition>, Boolean>> theorems;
	private Pair<Pair<Element, Element>, Boolean> fill;

	public Namespace(String fileName) {
		super(fileName);
		
		tests = NewCollection.set();
		operators = NewCollection.set();
		renames = NewCollection.map();
		theorems = NewCollection.map();
		clearFill();

		try {
			addOperator(null, EqualityOperator.EQUALITY);
			addOperator(null, DisqualityOperator.DISQUALITY);
			addOperator(null, FalseOperator.FALSE);
			addOperator(null, TrueOperator.TRUE);
		} catch (DuplicateNameError e) {
			throw new AssertionError();
		}
	}

	public Namespace(Lexer lex, Namespace space) throws DuplicateNameError, NoSuchNameError {
		this(space.getName());
		importSpace(lex, space, true);
	}

	public void importSpace(Lexer lex, Namespace space, boolean importAll) throws DuplicateNameError, NoSuchNameError {
		for(Operator<?> operator : space.operators){
			if(operators.contains(operator)) continue;
			
			addOperator(lex, operator);
		}
		
		if(importAll) {
			renames.putAll(space.renames);
		}
		
		for(String theorem : space.theorems.keySet()) {
			if(theorems.get(theorem) != null && space.theorems.get(theorem).left.equals(theorems.get(theorem).left)) continue;
			if(!importAll && !space.theorems.get(theorem).right) continue;

			addTheorem(lex, theorem, space.theorems.get(theorem).left, false);
		}
	}



	private void confirmNameAvailability(Lexer lex, String name, boolean real) throws DuplicateNameError {
		for(TestOperator<?> test : tests) {
			if(name.equals(test.getName())) throw new DuplicateNameError(lex, name);
		}

		if(real) {	
			for(Operator<?> operator : operators) {
				if(name.equals(operator.getName())) throw new DuplicateNameError(lex, name);
			}
			for(String rename : renames.keySet()) {
				if(name.equals(rename)) throw new DuplicateNameError(lex, name);
			}
			for(String string : theorems.keySet()) {
				if(name.equals(string)) throw new DuplicateNameError(lex, name);
			}
		}
	}



	public <E extends Entity<E>> void addTestOperator(TestOperator<E> operator, Lexer lex) throws DuplicateNameError {
		confirmNameAvailability(lex, operator.getName(), false);
		tests.add(operator);
	}

	public void removeTestOperator(TestOperator<?> operator) {
		tests.remove(operator);
	}

	public <E extends Entity<E>> void removeTestOperators(List<TestOperator<E>> operators) {
		for(TestOperator<E> operator : operators) {
			removeTestOperator(operator);
		}
	}



	public Operator<Element> addElement(Lexer lex, String name) throws DuplicateNameError {
		Operator<Element> element = new RealOperator<Element>(name, PlatonicArguments.EMPTY, Element.class, null);
		addOperator(lex, element);
		return element;
	}
	
	public <E extends Entity<E>> void addOperator(Lexer lex, Operator<E> operator) throws DuplicateNameError {
		confirmNameAvailability(lex, operator.getName(), true);		
		operators.add(operator);
	}
	
	public void renameOperator(Lexer lex, String name, Class<? extends Entity<?>> klass) throws SyntaxError {
		confirmNameAvailability(lex, name, true);		
		
		Operator<?> operator = getOperator(lex);
		if(klass != operator.getConverterClass()) throw new OperatorDeclerationError(lex, "Cannot declare operator with different type as original");
		
		renames.put(name, operator);
	}

	public Operator<?> getOperator(Lexer lex) throws NoSuchNameError, UnexpectedTokenError {
		String name = lex.nextName(true);
		
		for(TestOperator<?> test : tests) {
			if(test.getName().equals(name)) return test;
		}

		for(Operator<?> operator : operators) {
			if(name.equals(operator.getName())) return operator;
		}
		
		if(renames.containsKey(name)) return renames.get(name);

		throw new NoSuchNameError(lex, name, StringType.OPERATOR);
	}


	

	public void addTheorem(Lexer lex, String name, Definition<Proposition> theorem, boolean isNew) throws DuplicateNameError {
		if(theorems.containsKey(name)) throw new DuplicateNameError(lex, name);
		theorems.put(name, NewCollection.pair(theorem, isNew));
	}

	public Definition<Proposition> getTheorem(Lexer lex, String name) throws NoSuchNameError {
		Pair<Definition<Proposition>, Boolean> theorem = theorems.get(name);
		if(theorem == null) throw new NoSuchNameError(lex, name, StringType.THEOREM);
		return theorem.left;
	}


	
	
	public void clearFill() {
		fill = NewCollection.pair(NewCollection.pair(null, null), false);
	}
	
	public void primeFill() {
		if(!fill.right) clearFill();
		else fill.right = false;
	}
	
	public void setFill(Element left, Element right) {
		fill = NewCollection.pair(NewCollection.pair(left, right), true);
	}

	public Pair<Element, Element> getFill() {
		return fill.left;
	}
	
	
	@Override
	public String toString() {
		String str = new String();
	
		for(String theoremName : theorems.keySet()) {
			str += theorems.get(theoremName).left.toDevString(theoremName) + Constants.NEW_LINE;
		}
		
		return str;
	}
}
