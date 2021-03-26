package prover.structure;

import java.util.Map;
import java.util.Set;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.converter.operator.standard.test.element.TestElement;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.binary.flippable.equality.EqualityProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;

public abstract class Structure {	
	
	public abstract String getName();
	
	public abstract Set<TestElement> getContainedTestElements();
	
	public abstract Set<TestOperator<?>> getContainedTestOperators();
	
	public abstract Structure cleanTests();
	
	
	
	public abstract Set<Element> getAllElements();
	
	public abstract Set<Definition<?>> getAllDefinitions();

	public abstract Structure substituteOperators(OperatorDefinitionMap map);

	

	public abstract Set<OperatorDefinitionMap> assignTestOperators(Structure struct, Set<TestOperator<?>> testOperators, Map<TestOperator<?>, TestOperator<?>> testMap);

	public abstract Set<EqualityProposition> getDifferenceEquality(Structure struct, Map<TestOperator<?>, TestOperator<?>> testMap);
	
	public abstract Set<Set<EqualityProposition>> getDifferenceEqualityPlural(Structure struct, Map<TestOperator<?>, TestOperator<?>> testMap);


	
	public final boolean equals(Object obj) {
		return equals(obj, NewCollection.map());
	}
	public abstract boolean equals(Object obj, Map<TestOperator<?>, TestOperator<?>> testMap);

	@Override
	public abstract int hashCode();
	
	@Override
	public abstract String toString();

}
