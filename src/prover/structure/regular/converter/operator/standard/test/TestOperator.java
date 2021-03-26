package prover.structure.regular.converter.operator.standard.test;

import java.util.Set;

import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.StandardOperator;
import prover.structure.regular.entity.Entity;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.PlatonicArguments;

public class TestOperator<E extends Entity<E>> extends StandardOperator<E> {

	static int count = 0;
	
	public static int count() {
		count++;		
		return count;
	}

	public TestOperator(PlatonicArguments arguments, Class<E> klass) {
		this(Constants.TEST + "" + count(), arguments, klass);
	}

	public TestOperator(String name, PlatonicArguments arguments, Class<E> klass) {
		super(name, arguments, klass);
	}

	@Override
	public Set<Definition<?>> getAllDefinitionsHelper() {
		return NewCollection.set();
	}


}
