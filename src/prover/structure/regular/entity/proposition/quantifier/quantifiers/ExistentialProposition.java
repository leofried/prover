package prover.structure.regular.entity.proposition.quantifier.quantifiers;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.test.element.TestElement;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.quantifier.QuantifierProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.TestArguments;

public final class ExistentialProposition extends QuantifierProposition {
	
	public ExistentialProposition(TestElement instance, Proposition prop) {
		super(instance, prop);
	}

	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return new ExistentialProposition(tests.getElements().get(0), (Proposition) subs.get(0));
	}

	@Override
	public final Proposition applyElementExistential(Element element) {
		return getProp().substituteOperators(new OperatorDefinitionMap(getInstance(), new Definition<Element>(element)));
	}	

	@Override
	protected final String open() {
		return Constants.EXISTS.left;
	}

	@Override
	protected final String close() {
		return Constants.EXISTS.right;
	}
	
}
