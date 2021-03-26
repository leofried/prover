package prover.instruction.justification.justifications;

import java.util.Set;

import prover.error.logic.logics.AmbiguousDefinitionError;
import prover.instruction.justification.Justification;
import prover.state.base.bases.TheoremBase;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class DefinitionJustification extends Justification {

	private static DefinitionJustification instance = null;

	public static DefinitionJustification getInstance() {
		if(instance == null) instance = new DefinitionJustification();
		return instance;
	}

	private DefinitionJustification() {}

	@Override
	public Set<Pair<Proposition, Proposition>> getTruths(String loc, TheoremBase base, Proposition prop) {
		Set<Pair<Proposition, Proposition>> truths = NewCollection.set();
		for(Definition<Proposition> definition : base.getJustifications()) {
			try {
				truths.addAll(new EmptyTheoremJustification(definition).getTruths(loc, base, prop));
			} catch (AmbiguousDefinitionError e) {}
		}
		return truths;
	}
}
