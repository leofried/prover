package prover.structure.regular.converter.operator.standard.standards;

import prover.structure.regular.converter.operator.standard.StandardOperator;
import prover.structure.regular.entity.Entity;
import prover.utility.utilities.PlatonicArguments;

public class RealOperator<E extends Entity<E>> extends StandardOperator<E> {

	public RealOperator(String name, PlatonicArguments arguments, Class<E> klass, Integer strength) {
		super(name, arguments, klass, strength);
	}

}
