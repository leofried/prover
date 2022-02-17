package prover.structure.regular.converter;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.RegularStructure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.interfaces.Argumented;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;

public abstract class Converter<C extends Converter<C, E>, E extends Entity<E>> extends RegularStructure<C> implements Argumented {

	public Converter(TestArguments arguments, Structure struct) {
		super(arguments, NewCollection.list(struct));
	}
	
	public abstract Class<E> getConverterClass();

	@Override
	public abstract PlatonicArguments getArguments();

	public abstract E convert(List<Definition<Proposition>> predicates, List<Definition<Element>> functions, List<Element> elements);

	public final E convert(List<Definition<Proposition>> predicates, List<Definition<Element>> functions) {
		return convert(predicates, functions, NewCollection.list());
	}
	
	public final E convert(List<Element> elements) {
		return convert(NewCollection.list(), NewCollection.list(), elements);
	}
	
	public final E convert() {
		return convert(NewCollection.list(), NewCollection.list(), NewCollection.list());
	}
	
	public final E convert(TestArguments arguments) {
		return convert(arguments.getPredicateDefinitions(), arguments.getFunctionDefinitions(), convertHelper(arguments.getElements()));
	}
	
	public static List<Element> convertHelper(List<? extends Operator<Element>> testElements) {
		List<Element> elements = NewCollection.list();
		for(Operator<Element> testElement : testElements) {
			elements.add(testElement.convert());
		}
		return elements;
	}
}

