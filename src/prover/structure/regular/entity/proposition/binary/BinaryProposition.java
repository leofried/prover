package prover.structure.regular.entity.proposition.binary;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.TestArguments;

public abstract class BinaryProposition<E extends Entity<E>> extends Proposition {

	public BinaryProposition(E left, E right) {
		super(TestArguments.EMPTY, NewCollection.list(left, right));
	}
	
	public final E getLeft() {
		return getLeft(getSubstructures());
	}
	
	public final E getRight() {
		return getRight(getSubstructures());
	}
	
	protected final E getLeft(List<Structure> subs) {
		return (E) subs.get(0);
	}
	
	protected final E getRight(List<Structure> subs) {
		return (E) subs.get(1);
	}

	@Override
	public final String toString() {
		return Constants.PARENS.left + getLeft() + Constants.SPACE + stringName() + Constants.SPACE + getRight() + Constants.PARENS.right;
	}

	protected abstract String stringName();
}
