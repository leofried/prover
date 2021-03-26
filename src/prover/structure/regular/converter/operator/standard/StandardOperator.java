package prover.structure.regular.converter.operator.standard;

import java.util.List;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.predicate.PredicateProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.PlatonicArguments;

public abstract class StandardOperator<E extends Entity<E>> extends Operator<E> {
	
	public StandardOperator(String name, PlatonicArguments arguments, Class<E> klass) {
		super(name, arguments, klass);
	}
	
	@Override
	public E convert(List<Definition<Proposition>> predicates, List<Definition<Element>> functions, List<Element> elements) {
		return (E) NewCollection.chooseEntityType(getConverterClass(),
				new PredicateProposition((Operator<Proposition>) this, predicates, functions, elements),
				new Element((Operator<Element>) this, predicates, functions, elements));
	}
	
}
