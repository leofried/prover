package prover.structure.regular.converter.operator;

import java.util.List;

import prover.structure.Structure;
import prover.structure.name.NameStructure;
import prover.structure.regular.converter.Converter;
import prover.structure.regular.entity.Entity;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;

public abstract class Operator<E extends Entity<E>> extends Converter<Operator<E>, E> {

	private PlatonicArguments arguments;
	private Class<E> klass;
	private Integer strength;
	
	public Operator(String name, PlatonicArguments arguments, Class<E> klass, Integer strength) {
		super(TestArguments.EMPTY, new NameStructure(name));
		this.arguments = arguments;
		this.klass = klass;
		this.strength = strength;
	}

	@Override
	protected Operator<E> newStructure(TestArguments tests, List<Structure> subs) {
		return this;
	}
	
	@Override
	public Class<E> getConverterClass() {
		return klass;
	}

	@Override
	public PlatonicArguments getArguments() {
		return arguments;
	}
	
	public Integer getStrength() {
		return strength;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}