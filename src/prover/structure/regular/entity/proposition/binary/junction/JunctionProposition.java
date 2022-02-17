package prover.structure.regular.entity.proposition.binary.junction;

import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.BinaryProposition;

public abstract class JunctionProposition extends BinaryProposition<Proposition> {

	public JunctionProposition(Proposition left, Proposition right) {
		super(left, right);
	}

}
