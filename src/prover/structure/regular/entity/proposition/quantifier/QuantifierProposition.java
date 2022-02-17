package prover.structure.regular.entity.proposition.quantifier;

import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.TestArguments;

public abstract class QuantifierProposition extends Proposition {
	
	public QuantifierProposition(TestOperator<Element> instance, Proposition prop) {
		super(new TestArguments(instance), NewCollection.list(prop));
	}
	
	public final Proposition getProp() {
		return (Proposition) getSubstructures().get(0);
	}
	
	public final TestOperator<Element> getInstance() {
		return getTests().getElements().get(0);
	}
	
	
	@Override
	public final String toString() {
		return open() + getInstance() + close() + Constants.PARENS.left + getProp() + Constants.PARENS.right;
	}

	protected abstract String open();
	
	protected abstract String close();

}