package prover.structure.regular.entity;

import java.util.List;

import prover.structure.Structure;
import prover.structure.regular.RegularStructure;
import prover.utility.utilities.TestArguments;

public abstract class Entity<E extends Entity<E>> extends RegularStructure<E> {
	
	public Entity(TestArguments arguments, List<Structure> subs) {
		super(arguments, subs);
	}
	
}
