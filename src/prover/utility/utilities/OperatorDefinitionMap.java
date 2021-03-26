package prover.utility.utilities;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.utility.Utility;

public class OperatorDefinitionMap extends Utility {
	

	public static OperatorDefinitionMap combineMaps(OperatorDefinitionMap mapOne, OperatorDefinitionMap mapTwo){
		if(mapOne == null || mapTwo == null) return null;

		OperatorDefinitionMap ret = new OperatorDefinitionMap(mapOne);
		for(Operator<?> op : mapTwo.keySet()) {
			if(!ret.containsKey(op)) ret.map.put(op, mapTwo.get(op));
			else if(ret.get(op).equals(mapTwo.get(op))) continue;
			else return null;
		}

		return ret;
	}
	
	public static Set<OperatorDefinitionMap> combineMaps(Set<OperatorDefinitionMap> setOne, Set<OperatorDefinitionMap> setTwo){
		if(setOne == null || setTwo == null) return null;

		Set<OperatorDefinitionMap> ret = NewCollection.set();
		for(OperatorDefinitionMap mapOne : setOne) {
			for(OperatorDefinitionMap mapTwo : setTwo) {				
				OperatorDefinitionMap map = combineMaps(mapOne, mapTwo);
				if(map != null) ret.add(map);
			}
		}

		return ret;
	}
	
	private Map<Operator<?>, Definition<?>> map;
	
	public OperatorDefinitionMap(){
		map = NewCollection.map();
	}
	
	public <E extends Entity<E>> OperatorDefinitionMap(Operator<E> operator, Definition<E> definition){
		this();
		put(operator, definition);
	}
	
	public <E extends Entity<E>> OperatorDefinitionMap(List<Operator<E>> operators, List<Definition<E>> definitions){
		this();
		for(int i=0; i<operators.size(); i++) {
			put(operators.get(i), definitions.get(i));
		}
	}
	
	public OperatorDefinitionMap(Map<Operator<Element>, Operator<Element>> operators) {
		this();
		for(Entry<Operator<Element>, Operator<Element>> entry : operators.entrySet()) {
			map.put(entry.getKey(), new Definition<Element>(entry.getValue()));
		}
	}
	
	public OperatorDefinitionMap(OperatorDefinitionMap map) {
		this();
		putAll(map);
	}

	public <E extends Entity<E>> void put(Operator<E> operator, Definition<E> definition){
		map.put(operator, definition);
	}
	
	public void putAll(OperatorDefinitionMap map) {
		this.map.putAll(map.map);
	}
	
	public <E extends Entity<E>> void remove(Operator<E> operator) {
		map.remove(operator);
	}
	
	public <E extends Entity<E>> Definition<E> get(Operator<E> operator){
		return (Definition<E>) map.get(operator);
	}
	
	public boolean containsKey(Object obj) {
		return map.containsKey(obj);
	}
	
	public Set<Operator<?>> keySet(){
		return map.keySet();
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return map.equals(((OperatorDefinitionMap) obj).map);
	}
	
	@Override
	public int hashCode() {
		return map.hashCode();
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
	
}
