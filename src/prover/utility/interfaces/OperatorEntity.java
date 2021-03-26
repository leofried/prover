package prover.utility.interfaces;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;

public interface OperatorEntity<E extends Entity<E>> {

	public List<Structure> getSubsHelper();
	
	public default Operator<E> getOperator() {
		return (Operator<E>) getSubsHelper().get(0);
	}

	public default List<Definition<Proposition>> getPredicates() {
		Operator<?> operator = getOperator();

		List<Definition<Proposition>> definitions = NewCollection.list();
		for(int i=1; i<1 + operator.getArguments().getPredicates().size(); i++) {
			definitions.add((Definition<Proposition>) getSubsHelper().get(i));
		}
		return definitions;
	}

	public default List<Definition<Element>> getFunctions() {
		Operator<?> operator = getOperator();

		List<Definition<Element>> definitions = NewCollection.list();
		for(int i=1 + operator.getArguments().getPredicates().size(); i<1 + operator.getArguments().getPredicates().size() + operator.getArguments().getFunctions().size(); i++) {
			definitions.add((Definition<Element>) getSubsHelper().get(i));
		}
		return definitions;
	}

	public default List<Element> getElements() {
		Operator<?> operator = getOperator();

		List<Element> elements = NewCollection.list();
		for(int i=getSubsHelper().size() - operator.getArguments().getElements(); i<getSubsHelper().size(); i++) {
			elements.add((Element) getSubsHelper().get(i));
		}
		return elements;
	}

	public default E substitution(OperatorDefinitionMap map) {
		return map.get(getOperator()).convert(getPredicates(), getFunctions(), getElements());
	}

	public default String toStringHelper() {
		String str = getOperator().toString();
		if(getPredicates().size() != 0) str += Constants.PREDICATES.left + getPredicates().toString().substring(1, getPredicates().toString().length() - 1) + Constants.PREDICATES.right;
		if(getFunctions() .size() != 0) str += Constants.FUNCTIONS .left + getFunctions ().toString().substring(1, getFunctions ().toString().length() - 1) + Constants.FUNCTIONS .right;
		if(getElements().size() != 0) {
			str += Constants.ELEMENTS.left;
			for(Element e : getElements()) {
				str += e.toString() + Constants.COMMA +  " ";
			}
			str = str.substring(0, str.length() - 2) + Constants.ELEMENTS.right;
		}
		return str;
	}

}
