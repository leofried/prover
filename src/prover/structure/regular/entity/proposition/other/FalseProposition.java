package prover.structure.regular.entity.proposition.other;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.TestArguments;

public class FalseProposition extends Proposition {
	
	public static final Proposition FALSE = new FalseProposition();
	
	private FalseProposition() {
		super(TestArguments.EMPTY, NewCollection.list());
	}

	@Override
	protected final Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return FALSE;
	}
	
	@Override
	public Proposition not() {
		return TrueProposition.TRUE;
	}

	@Override
	public final String toString() {
		return Constants.FALSE;
	}
}
