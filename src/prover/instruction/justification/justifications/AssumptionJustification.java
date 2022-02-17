package prover.instruction.justification.justifications;

import java.util.List;

import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class AssumptionJustification extends Justification {
	
	public AssumptionJustification() {}
	
	@Override
	public List<Pair<Proposition, Pair<Proposition, Integer>>> getTruths(String loc, TheoremBase base, Proposition prop) {
		List<Pair<Proposition, Pair<Proposition, Integer>>> list = NewCollection.list();
		int i=0;
		for(Proposition goal : base.getGoals()) {
			for(Pair<Proposition, Proposition> pair : goal.getAssumptionList()) {
				list.add(NewCollection.pair(pair.left, NewCollection.pair(pair.right, i)));
			}
			i++;
		}
		return list;
	}
}
