package prover.structure.regular;

import java.util.List;
import java.util.Map;
import java.util.Set;

import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.BinaryProposition;
import prover.structure.regular.entity.proposition.binary.equality.EqualityProposition;
import prover.structure.regular.entity.proposition.other.NegationProposition;
import prover.utility.interfaces.OperatorEntity;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.TestArguments;

public abstract class RegularStructure<S extends RegularStructure<S>> extends Structure {

	private TestArguments tests;
	private List<Structure> subs;

	public RegularStructure(TestArguments tests, List<Structure> subs) {
		this.tests = tests;
		this.subs = subs;
	}

	protected abstract S newStructure(TestArguments tests, List<Structure> subs);

	protected final TestArguments getTests(){
		return tests;
	}

	protected final List<Structure> getSubstructures(){
		return NewCollection.list(subs);
	}

	private List<RegularStructure<?>> getEqualableStructs(Structure struct, Map<TestOperator<?>, TestOperator<?>> testMap){
		if(getClass() != struct.getClass()) return NewCollection.list();
		RegularStructure<?> regStruct = (RegularStructure<?>) struct;
		if(getTests().getPredicates().size() != regStruct.getTests().getPredicates().size()) return NewCollection.list();
		if(getTests().getFunctions().size() != regStruct.getTests().getFunctions().size()) return NewCollection.list();
		if(getTests().getElements().size() != regStruct.getTests().getElements().size()) return NewCollection.list();
		if(this instanceof OperatorEntity && !((OperatorEntity<?>) this).getOperator().getArguments().equals(((OperatorEntity<?>) struct).getOperator().getArguments())) return NewCollection.list();

		for(int i=0; i<getTests().getPredicates().size(); i++) {
			testMap.put(getTests().getPredicates().get(i), regStruct.getTests().getPredicates().get(i));
		}
		for(int i=0; i<getTests().getFunctions().size(); i++) {
			testMap.put(getTests().getFunctions().get(i), regStruct.getTests().getFunctions().get(i));
		}
		for(int i=0; i<getTests().getElements().size(); i++) {
			testMap.put(getTests().getElements().get(i), regStruct.getTests().getElements().get(i));
		}

		List<RegularStructure<?>> list = NewCollection.list(regStruct);
		if(regStruct instanceof BinaryProposition) list.add(regStruct.newStructure(getTests(), NewCollection.list(regStruct.getSubstructures().get(1), regStruct.getSubstructures().get(0))));
		return list;
	}



	@Override
	public final String getName() {
		return getSubstructures().get(0).getName();
	}

	private Set<TestOperator<?>> getContainedTestOperators = null;
	@Override
	public final Set<TestOperator<?>> getContainedTestOperators() {
		if(getContainedTestOperators != null) return getContainedTestOperators;
		Set<TestOperator<?>> set = NewCollection.set();

		for(Structure sub : getSubstructures()){
			set.addAll(sub.getContainedTestOperators());
		}

		if(this instanceof TestOperator) set.add((TestOperator<?>) this);
		set.removeAll(getTests().getPredicates());
		set.removeAll(getTests().getFunctions());
		set.removeAll(getTests().getElements());

		getContainedTestOperators = set;
		return set;
	}

	@Override
	public final S cleanTests() {
		TestArguments newTests = new TestArguments(getTests().getArguments());

		List<Structure> newSubs = NewCollection.list();

		for(Structure sub : getSubstructures()) {
			newSubs.add(sub.substituteOperators(new OperatorDefinitionMap(getTests(), newTests)).cleanTests());
		}

		return newStructure(newTests, newSubs);
	}




	private Set<Element> getAllElements = null;
	@Override
	public final Set<Element> getAllElements() {
		if(getAllElements != null) return getAllElements;
		Set<Element> set = NewCollection.set();	

		for(Structure sub : getSubstructures()) {
			set.addAll(sub.getAllElements());
		}

		if(this instanceof Element && getContainedTestOperators().isEmpty()) {
			set.add((Element) this);
		}

		getAllElements = set;
		return set;
	}



	
	private Set<Definition<?>> getAllDefinitions = null;
	@Override
	public final Set<Definition<?>> getAllDefinitions() {
		if(getAllDefinitions != null) return getAllDefinitions;
		Set<Definition<?>> set = NewCollection.set();		

		for(Structure sub : getSubstructures()) {
			set.addAll(sub.getAllDefinitions());
		}

		if(this instanceof Definition && getContainedTestOperators().isEmpty()) {
			set.add((Definition<?>) this);
		}

		getAllDefinitions = set;
		return set;
	}


	@Override
	public final S substituteOperators(OperatorDefinitionMap map) {
		if(this instanceof OperatorEntity && map.containsKey(((OperatorEntity<?>) this).getOperator())) {
			return (S) ((OperatorEntity<?>) this).substitution(map).cleanTests().substituteOperators(map);
		} else if(this instanceof NegationProposition) {
			return (S) ((Proposition) getSubstructures().get(0)).substituteOperators(map).not();
		} else {
			List<Structure> newSubs = NewCollection.list();

			for(Structure sub : getSubstructures()) {
				newSubs.add(sub.substituteOperators(map));
			}

			S newStruct = newStructure(getTests(), newSubs);

			return newStruct;
		}
	}



	public final Set<OperatorDefinitionMap> assignTestOperators(Structure struct, Set<TestOperator<?>> testOperators) {
		return assignTestOperators(struct, testOperators, NewCollection.map());
	}	
	@Override
	public final Set<OperatorDefinitionMap> assignTestOperators(Structure struct, Set<TestOperator<?>> testOperators, Map<TestOperator<?>, TestOperator<?>> testMap) {
		if(struct.equals(testMap.get(this))) return NewCollection.set(new OperatorDefinitionMap());

		Set<OperatorDefinitionMap> set = NewCollection.set();

		for(RegularStructure<?> eqStruct : getEqualableStructs(struct, testMap)) {
			Set<OperatorDefinitionMap> tempSet = NewCollection.set(new OperatorDefinitionMap());

			for(int i=0; i<getSubstructures().size(); i++) {
				tempSet = OperatorDefinitionMap.combineMaps(tempSet,
						getSubstructures().get(i).assignTestOperators(
								eqStruct.getSubstructures().get(i), testOperators, NewCollection.map(testMap)));
			}

			set.addAll(tempSet);
		}

		if(this instanceof Element && testOperators.contains(((Element) this).getOperator())) {
			set.add(new OperatorDefinitionMap(((Element) this).getOperator(), new Definition<Element>((Element) struct)));
		}

		return set;
	}

	public final Set<EqualityProposition> getDifferenceEquality(Structure struct){
		return getDifferenceEquality(struct, NewCollection.map());
	}

	@Override
	public final Set<EqualityProposition> getDifferenceEquality(Structure struct, Map<TestOperator<?>, TestOperator<?>> testMap) {
		if(struct.equals(testMap.get(this))) {
			return null;
		}

		Set<EqualityProposition> set = NewCollection.set();

		for(RegularStructure<?> eqStruct : getEqualableStructs(struct, testMap)) {
			Set<EqualityProposition> tempSet = null;

			for(int i=0; i<getSubstructures().size(); i++) {
				tempSet = NewCollection.intersect(tempSet,
						getSubstructures().get(i).getDifferenceEquality(
								eqStruct.getSubstructures().get(i), NewCollection.map(testMap)));
			}

			if(tempSet == null) return null;
			else set.addAll(tempSet);
		}

		if(this instanceof Element) set.add(new EqualityProposition((Element) this, (Element) struct));

		return set;
	}	


	public final Set<Set<EqualityProposition>> getDifferenceEqualityPlural(Structure struct){
		return getDifferenceEqualityPlural(struct, NewCollection.map());
	}
	@Override
	public final Set<Set<EqualityProposition>> getDifferenceEqualityPlural(Structure struct, Map<TestOperator<?>, TestOperator<?>> testMap) {
		if(struct.equals(testMap.get(this))) {
			return null;
		}

		Set<Set<EqualityProposition>> set = NewCollection.set();

		for(RegularStructure<?> eqStruct : getEqualableStructs(struct, testMap)) {
			Set<Set<EqualityProposition>> tempSet = null;

			for(int i=0; i<getSubstructures().size(); i++) {
				tempSet = NewCollection.product(tempSet,
						getSubstructures().get(i).getDifferenceEqualityPlural(
								eqStruct.getSubstructures().get(i), NewCollection.map(testMap)));
			}

			if(tempSet == null) return null;
			else set.addAll(tempSet);
		}

		if(this instanceof Element) set.add(NewCollection.set(new EqualityProposition((Element) this, (Element) struct)));

		return set;
	}	




	public final boolean equals(Object obj, Map<TestOperator<?>, TestOperator<?>> testMap) {		
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		if (obj.equals(testMap.get(this))) return true;

		if(this instanceof TestOperator) return false;

		for(RegularStructure<?> eqStruct : getEqualableStructs((Structure) obj, testMap)) {
			boolean eq = true;
			for(int i=0; i<getSubstructures().size(); i++) {
				if(!getSubstructures().get(i).equals(eqStruct.getSubstructures().get(i), NewCollection.map(testMap))) eq = false;
			}
			if(eq) return true;
		}

		return false;
	}

	private Integer hashCode = null;
	@Override
	public int hashCode() {
		if(hashCode != null) return hashCode;
		if(this instanceof TestOperator) {
			hashCode = 0;
		}else if(this instanceof BinaryProposition) {
			hashCode = getSubstructures().get(0).hashCode() * getSubstructures().get(1).hashCode();
		}else {
			hashCode = getSubstructures().hashCode();
		}
		return hashCode;
	}


	@Override
	public abstract String toString();

}
