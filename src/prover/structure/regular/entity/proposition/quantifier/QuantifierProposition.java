package prover.structure.regular.entity.proposition.quantifier;

import java.util.Set;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.test.element.TestElement;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.implication.ImplicationProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.UniversalProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.TestArguments;

public abstract class QuantifierProposition extends Proposition {
	
	public QuantifierProposition(TestElement instance, Proposition prop) {
		super(new TestArguments(instance), NewCollection.list(prop));
	}
	
	protected final Proposition getProp() {
		return (Proposition) getSubstructures().get(0);
	}
	
	protected final TestElement getInstance() {
		return getTests().getElements().get(0);
	}
	
	@Override
	protected final Set<Definition<Proposition>> getAllDefinitionsHelperHelper() {
		Set<Definition<Proposition>> set = NewCollection.set(new Definition<Proposition>(getTests(), getProp()));
		
		/*
		 * Induction
		 */
		if(this instanceof UniversalProposition && getProp() instanceof ImplicationProposition) {
			set.add(new Definition<Proposition>(getTests(), ((ImplicationProposition) getProp()).getRight()));
		}
		
		return set;
	}
	
	@Override
	public final String toString() {
		return open() + getInstance() + close() + Constants.PARENS.left + getProp() + Constants.PARENS.right;
	}

	protected abstract String open();
	
	protected abstract String close();

}