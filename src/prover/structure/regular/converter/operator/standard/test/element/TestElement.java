package prover.structure.regular.converter.operator.standard.test.element;

import prover.structure.regular.converter.operator.standard.test.TestOperator;
import prover.structure.regular.entity.element.Element;
import prover.utility.utilities.PlatonicArguments;

public class TestElement extends TestOperator<Element> {

	public TestElement(String name) {
		super(name, new PlatonicArguments(), Element.class);
	}
	
	public TestElement() {
		super(new PlatonicArguments(), Element.class);
	}
}