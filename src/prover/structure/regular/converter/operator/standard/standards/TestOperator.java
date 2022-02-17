package prover.structure.regular.converter.operator.standard.standards;

import prover.structure.regular.converter.operator.standard.StandardOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.utility.utilities.Constants;
import prover.utility.utilities.PlatonicArguments;

public class TestOperator<E extends Entity<E>> extends StandardOperator<E> {
	
	static int count = 0;
	
	public static String name() {
		count++;		
		return Constants.TEST + count;
	}
	
	public TestOperator() {
		this(name());
	}
	
	public TestOperator(String name) {
		this(name, PlatonicArguments.EMPTY, (Class<E>) Element.class);
	}

	public TestOperator(PlatonicArguments arguments, Class<E> klass) {
		this(name(), arguments, klass);
	}

	public TestOperator(String name, PlatonicArguments arguments, Class<E> klass) {
		super(name, arguments, klass, null);
	}
}
