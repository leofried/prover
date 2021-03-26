package prover.structure.name;

import java.util.Map;
import java.util.Set;

import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.converter.operator.standard.test.element.TestElement;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.binary.flippable.equality.EqualityProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;

public class NameStructure extends Structure {

	private String name;
	
	public NameStructure(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Set<TestElement> getContainedTestElements() {
		return NewCollection.set();
	}
	
	@Override
	public Set<TestOperator<?>> getContainedTestOperators() {
		return  NewCollection.set();
	}
	
	@Override
	public Structure cleanTests() {
		return this;
	}
	
	@Override
	public Set<Element> getAllElements() {
		return NewCollection.set();
	}

	@Override
	public Set<Definition<?>> getAllDefinitions() {
		return NewCollection.set();
	}

	@Override
	public NameStructure substituteOperators(OperatorDefinitionMap map) {
		return this;
	}

	@Override
	public Set<OperatorDefinitionMap> assignTestOperators(Structure struct, Set<TestOperator<?>> testOperators, Map<TestOperator<?>, TestOperator<?>> testMap) {
		if(this == struct) return NewCollection.set(new OperatorDefinitionMap());
		else return NewCollection.set();
	}
	
	@Override
	public Set<EqualityProposition> getDifferenceEquality(Structure struct, Map<TestOperator<?>, TestOperator<?>> testMap) {
		if(this == struct) return null;
		else return NewCollection.set();
	}
	
	@Override
	public final Set<Set<EqualityProposition>> getDifferenceEqualityPlural(Structure struct, Map<TestOperator<?>, TestOperator<?>> testMap) {
		if(this == struct) return null;
		else return NewCollection.set();
	}

	@Override
	public boolean equals(Object obj, Map<TestOperator<?>, TestOperator<?>> testMap) {
		return this == obj;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

}
