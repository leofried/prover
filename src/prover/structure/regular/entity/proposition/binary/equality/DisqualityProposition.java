package prover.structure.regular.entity.proposition.binary.equality;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.BinaryProposition;
import prover.structure.regular.entity.proposition.other.FalseProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.TestArguments;

public class DisqualityProposition extends BinaryProposition<Element> {

	public DisqualityProposition(Element left, Element right) {
		super(left, right);
	}

	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return new DisqualityProposition(getLeft(subs), getRight(subs));
	}
	
	@Override
	public Proposition not() {
		return new EqualityProposition(getLeft(), getRight());
	}
	
	@Override
	public final List<Pair<Proposition, Proposition>> getAssumptionList() {
		return NewCollection.list(NewCollection.pair(not(), FalseProposition.FALSE));
	}
	
	@Override
	protected final String stringName() {
		return Constants.DISQUALITY;
	}
}
