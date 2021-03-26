package prover.state.space;

import java.util.List;
import java.util.Map;
import java.util.Set;

import prover.error.syntax.SyntaxError.StringType;
import prover.error.syntax.syntaxes.DuplicateNameError;
import prover.error.syntax.syntaxes.NoSuchNameError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.reader.readers.Lexer;
import prover.state.State;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.converter.operator.standard.real.RealOperator;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;

public class Namespace extends State {
	
	private Set<TestOperator<?>> tests;
	private Map<Operator<?>, Boolean> operators;
	private OperatorDefinitionMap sugars;
	private Map<String, Pair<Definition<Proposition>, Boolean>> theorems;
	private Pair<Pair<Element, Element>, Boolean> fill;

	public Namespace() {
		tests = NewCollection.set();
		operators = NewCollection.map();
		sugars = new OperatorDefinitionMap();
		theorems = NewCollection.map();
		clearFill();

		try {
			addOperator(null, Operator.TRUE, null, false);
			addOperator(null, Operator.FALSE, null, false);
			addOperator(null, Operator.EQUALITY, null, false);
		} catch (DuplicateNameError e) {
			throw new AssertionError();
		}
	}

	public Namespace(Lexer lex, Namespace space) throws DuplicateNameError, NoSuchNameError {
		this();
		importSpace(lex, space, null);
	}

	public void importSpace(Lexer lex, Namespace space, String nameToImport) throws DuplicateNameError, NoSuchNameError {
		boolean imported = nameToImport == null || nameToImport.equals("");
		
		for(Operator<?> operator : space.operators.keySet()){
			if(operators.containsKey(operator)) continue;
			if(nameToImport != null){
				if(!space.operators.get(operator)) continue;
				if(!nameToImport.equals("") && !nameToImport.equals(operator.getName())) continue;
			}
			addOperator(lex, operator, space.sugar(operator), false);
			imported = true;
		}
		
		for(String theorem : space.theorems.keySet()) {
			if(theorems.get(theorem) != null && space.theorems.get(theorem).left.equals(theorems.get(theorem).left)) continue;
			if(nameToImport != null) {
				if(!space.theorems.get(theorem).right) continue;
				if(!nameToImport.equals("") && !nameToImport.equals(theorem)) continue;
			}
			addTheorem(lex, theorem, space.theorems.get(theorem).left, false);
			imported = true;
		}
		
		if(!imported) throw new NoSuchNameError(lex, nameToImport, StringType.OPERATOR);
	}



	private void confirmNameAvailability(Lexer lex, String name, boolean real) throws DuplicateNameError {
		for(TestOperator<?> test : tests) {
			if(name.equals(test.getName())) throw new DuplicateNameError(lex, name);
		}

		if(real) {	
			for(Operator<?> operator : operators.keySet()) {
				if(name.equals(operator.getName())) throw new DuplicateNameError(lex, name);
			}
			for(String string : theorems.keySet()) {
				if(name.equals(string)) throw new DuplicateNameError(lex, name);
			}
		}
	}



	public <E extends Entity<E>> void addTestOperator(TestOperator<E> operator, Lexer lex) throws DuplicateNameError {
		confirmNameAvailability(lex, operator.getName(), false);
		tests.add(operator);
		sugars.put(operator, new Definition<E>(operator));
	}

	public void removeTestOperator(TestOperator<?> operator) {
		tests.remove(operator);
		sugars.remove(operator);
	}

	public <E extends Entity<E>> void removeTestOperators(List<TestOperator<E>> operators) {
		for(TestOperator<E> operator : operators) {
			removeTestOperator(operator);
		}
	}



	public Operator<Element> addElement(Lexer lex, String name) throws DuplicateNameError {
		Operator<Element> element = new RealOperator<Element>(name, new PlatonicArguments(), Element.class);
		addOperator(lex, element, null, true);
		return element;
	}

	public <E extends Entity<E>> void addOperator(Lexer lex, Operator<E> operator, Definition<?> sugar, boolean isNew) throws DuplicateNameError {
		confirmNameAvailability(lex, operator.getName(), true);

		operators.put(operator, isNew);
		
		if(sugar == null) sugar = new Definition<E>(operator);
		sugars.put(operator, (Definition<E>) sugar);	
	}

	public Operator<?> getOperator(Lexer lex) throws NoSuchNameError, UnexpectedTokenError {
		String name = lex.nextName(true);
		
		for(TestOperator<?> test : tests) {
			if(test.getName().equals(name)) return test;
		}

		for(Operator<?> operator : operators.keySet()) {
			if(name.equals(operator.getName())) return operator;
		}

		throw new NoSuchNameError(lex, name, StringType.OPERATOR);
	}

	public <E extends Entity<E>> Definition<E> sugar(Operator<E> operator){
		return sugars.get(operator).cleanTests();
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
	
}
