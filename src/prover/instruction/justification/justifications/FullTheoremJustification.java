package prover.instruction.justification.justifications;

import java.util.List;

import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class FullTheoremJustification extends Justification {

	private Definition<Proposition> theorem;
	private List<Definition<Proposition>> predicates;
	private List<Definition<Element>> functions;
	
	public FullTheoremJustification(Definition<Proposition> theorem, List<Definition<Proposition>> predicates, List<Definition<Element>> functions) {
		this.theorem = theorem;
		this.predicates = predicates;
		this.functions = functions;
	}

	@Override
	public List<Pair<Proposition, Pair<Proposition, Integer>>> getTruths(String loc, TheoremBase base, Proposition prop) {
		return NewCollection.list(NewCollection.pair(base.sugar(theorem.convert(predicates, functions)), NewCollection.pair(base.getGoal(), 0)));
	}
}
