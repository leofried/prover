package prover.structure.regular.entity.proposition.other;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.TestArguments;

public class NegationProposition extends Proposition {

	public NegationProposition(Proposition prop) {
		this(NewCollection.list(prop));
	}
	
	private NegationProposition(List<Structure> subs) {
		super(TestArguments.EMPTY, subs);
	}
	 
	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return new NegationProposition(subs);
	}
	
	@Override
	public final Proposition not() {
		return (Proposition) getSubstructures().get(0);
	}
	
	@Override
	public final List<Pair<Proposition, Proposition>> getAssumptionList() {
		return NewCollection.list(NewCollection.pair(not(), FalseProposition.FALSE));
	}
	
	@Override
	public final String toString() {
		return Constants.NOT + Constants.SPACE + not();
	}

}
