package prover.structure.regular.converter.operator.other;

import java.util.List;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.other.TrueProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.PlatonicArguments;

public class TrueOperator extends Operator<Proposition> {
	
	public static final Operator<Proposition> TRUE = new TrueOperator();
	
	private TrueOperator() {
		super(Constants.TRUE, PlatonicArguments.EMPTY, Proposition.class, null);
	}
	
	public Proposition convert(List<Definition<Proposition>> predicates, List<Definition<Element>> functions, List<Element> elements) {
		return TrueProposition.TRUE; 
	}
}
