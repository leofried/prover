package prover.instruction.justification.justifications;

import java.util.List;
import java.util.Set;

import prover.error.logic.logics.AmbiguousDefinitionError;
import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;

public class EmptyTheoremJustification extends Justification {

	private Definition<Proposition> theorem;

	public EmptyTheoremJustification(Definition<Proposition> theorem) {
		this.theorem = theorem;
	}

	@Override
	public Set<Pair<Proposition,Proposition>> getTruths(String loc, TheoremBase base, Proposition prop) throws AmbiguousDefinitionError {
		Set<Pair<Proposition, Proposition>> set = NewCollection.set();

		List<Set<Definition<Proposition>>> eligiblePredicates = NewCollection.list();
		List<Set<Definition<Element>>> eligibleFunctions = NewCollection.list();

		for(int i=0; i<theorem.getArguments().getPredicates().size(); i++) {
			PlatonicArguments arguments = theorem.getArguments().getPredicates().get(i);
			eligiblePredicates.add(base.getDefinitions(Proposition.class, arguments, loc));
		}
		for(int i=0; i<theorem.getArguments().getFunctions().size(); i++) {
			PlatonicArguments arguments = theorem.getArguments().getFunctions().get(i);
			eligibleFunctions.add(base.getDefinitions(Element.class, arguments, loc));
		}

		Set<List<Definition<Proposition>>> factorializedPredicates = NewCollection.factorialize(eligiblePredicates);
		Set<List<Definition<Element>>> factorializedFunctions = NewCollection.factorialize(eligibleFunctions);

		for(List<Definition<Proposition>> predicates : factorializedPredicates) {
			for(List<Definition<Element>> functions : factorializedFunctions) {
				set.add(NewCollection.pair(theorem.convert(predicates, functions), base.getGoal()));
			}
		}
		
		return set;
	}
}
