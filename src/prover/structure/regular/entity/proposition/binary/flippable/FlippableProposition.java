package prover.structure.regular.entity.proposition.binary.flippable;

import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.proposition.binary.BinaryProposition;

public abstract class FlippableProposition<E extends Entity<E>> extends BinaryProposition<E>{

	public FlippableProposition(E left, E right) {
		super(left, right);
	}

}
