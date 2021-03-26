package prover.structure.regular.entity.proposition.binary.flippable.equality;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.flippable.FlippableProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.TestArguments;

public class EqualityProposition extends FlippableProposition<Element> {

	public EqualityProposition(Element left, Element right) {
		super(left, right);
	}

	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return new EqualityProposition(getLeft(subs), getRight(subs));
	}

	@Override
	protected final String stringName() {
		return Constants.EQUALITY;
	}
}
