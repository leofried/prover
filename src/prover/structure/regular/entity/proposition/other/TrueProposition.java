package prover.structure.regular.entity.proposition.other;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.TestArguments;

public class TrueProposition extends Proposition {
	
	public static final Proposition TRUE = new TrueProposition();
	
	private TrueProposition() {
		super(TestArguments.EMPTY, NewCollection.list());
	}

	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return TRUE;
	}
	
	@Override
	public Proposition not() {
		return FalseProposition.FALSE;
	}
	
	@Override
	public final String toString() {
		return Constants.TRUE;
	}
}
