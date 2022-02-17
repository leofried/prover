package prover.instruction.justification.justifications;

import java.util.List;

import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class TruthsJustification extends Justification {

	public TruthsJustification() {}

	@Override
	public List<Pair<Proposition, Pair<Proposition, Integer>>> getTruths(String loc, TheoremBase base, Proposition prop) {
		if(prop.isTrue(base)) return null;

		List<Pair<Proposition, Pair<Proposition, Integer>>> truths = NewCollection.list();
		for(Proposition truth : base.getTruths()) {
			truths.add(NewCollection.pair(truth, NewCollection.pair(base.getGoal(), 0)));
		}
		return truths;
	}
}
