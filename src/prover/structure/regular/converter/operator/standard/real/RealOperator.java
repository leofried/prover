package prover.structure.regular.converter.operator.standard.real;

import java.util.Set;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.StandardOperator;
import prover.structure.regular.entity.Entity;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.PlatonicArguments;

public class RealOperator<E extends Entity<E>> extends StandardOperator<E> {

	public RealOperator(String name, PlatonicArguments arguments, Class<E> klass) {
		super(name, arguments, klass);
	}

	@Override
	public Set<Definition<?>> getAllDefinitionsHelper() {
		return NewCollection.set(new Definition<E>(this));
	}

}
