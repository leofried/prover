package prover.instruction.justification.justifications;

import java.util.List;
import java.util.Set;

import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.junction.junctions.DisjunctionProposition;
import prover.structure.regular.entity.proposition.quantifier.quantifiers.UniversalProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;

public class EmptyTheoremJustification extends Justification {

	private Definition<Proposition> theorem;

	public EmptyTheoremJustification(Definition<Proposition> theorem) {
		this.theorem = theorem;
	}

	@Override
	public List<Pair<Proposition, Pair<Proposition, Integer>>> getTruths(String loc, TheoremBase base, Proposition prop) {
		theorem = base.sugar(theorem);
		
		List<Pair<Proposition, Pair<Proposition, Integer>>> list = NewCollection.list();

		List<Set<Definition<Proposition>>> eligiblePredicates = NewCollection.list();
		List<Set<Definition<Element>>> eligibleFunctions = NewCollection.list();

		for(int i=0; i<theorem.getArguments().getPredicates().size(); i++) {
			PlatonicArguments arguments = theorem.getArguments().getPredicates().get(i);
			eligiblePredicates.add(base.getDefinitions(Proposition.class, arguments, loc));
			
			if(arguments.equals(PlatonicArguments.EMPTY)) {
				eligiblePredicates.get(i).add(new Definition<Proposition>(prop)); 
			} else if(arguments.equals(new PlatonicArguments(1))) {
				if(prop instanceof UniversalProposition) {
					UniversalProposition univ = (UniversalProposition) prop;
					eligiblePredicates.get(i).add(new Definition<Proposition>(new TestArguments(univ.getInstance()), univ.getProp()));
					if(univ.getProp() instanceof DisjunctionProposition) {
						eligiblePredicates.get(i).add(new Definition<Proposition>(new TestArguments(univ.getInstance()), ((DisjunctionProposition) univ.getProp()).getRight()));
					}
				}
			}
		
		}
		for(int i=0; i<theorem.getArguments().getFunctions().size(); i++) {
			PlatonicArguments arguments = theorem.getArguments().getFunctions().get(i);
			eligibleFunctions.add(base.getDefinitions(Element.class, arguments, loc));
		}

		Set<List<Definition<Proposition>>> factorializedPredicates = NewCollection.factorialize(eligiblePredicates);
		Set<List<Definition<Element>>> factorializedFunctions = NewCollection.factorialize(eligibleFunctions);

		for(List<Definition<Proposition>> predicates : factorializedPredicates) {
			for(List<Definition<Element>> functions : factorializedFunctions) {
				list.add(NewCollection.pair(theorem.convert(predicates, functions), NewCollection.pair(base.getGoal(), 0)));
			}
		}
		
		return list;
	}
}
