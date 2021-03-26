package prover.instruction.justification.justifications;

import java.util.List;
import java.util.Set;

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
	public Set<Pair<Proposition, Proposition>> getTruths(String loc, TheoremBase state, Proposition prop) {
		return NewCollection.set(NewCollection.pair(theorem.convert(predicates, functions), state.getGoal()));
	}
}
