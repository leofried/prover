package prover.structure.regular.converter.operator.other;

import java.util.List;

import prover.reader.readers.Parser;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.equality.EqualityProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.PlatonicArguments;

public class EqualityOperator extends Operator<Proposition> {
	
	public static final EqualityOperator EQUALITY = new EqualityOperator();
	
	private EqualityOperator() {
		super(Constants.EQUALITY, new PlatonicArguments(2), Proposition.class, Parser.PREDICATE_STRENGTH);
	}
	
	public Proposition convert(List<Definition<Proposition>> predicates, List<Definition<Element>> functions, List<Element> elements) {
		return new EqualityProposition(elements.get(0), elements.get(1)); 
	}
}

