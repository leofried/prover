package prover.instruction.justification.justifications;

import java.util.Set;

import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class TruthsJustification extends Justification {

	private static TruthsJustification instance = null;

	public static TruthsJustification getInstance() {
		if(instance == null) instance = new TruthsJustification();
		return instance;
	}

	private TruthsJustification() {}

	@Override
	public Set<Pair<Proposition, Proposition>> getTruths(String loc, TheoremBase base, Proposition prop) {
		if(prop.isTrue(base)) return null;

		Set<Pair<Proposition, Proposition>> truths = NewCollection.set();
		for(Proposition truth : base.getTruths()) {
			truths.add(NewCollection.pair(truth, base.getGoal()));
		}
		return truths;
	}
}
