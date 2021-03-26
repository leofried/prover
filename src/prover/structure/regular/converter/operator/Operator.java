package prover.structure.regular.converter.operator;

import java.util.List;

import prover.structure.Structure;
import prover.structure.name.NameStructure;
import prover.structure.regular.converter.Converter;
import prover.structure.regular.converter.operator.equality.EqualityOperator;
import prover.structure.regular.converter.operator.standard.real.RealOperator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.PlatonicArguments;
import prover.utility.utilities.TestArguments;

public abstract class Operator<E extends Entity<E>> extends Converter<Operator<E>, E> {

	public static final Operator<Proposition> TRUE = new RealOperator<Proposition>(Constants.TRUE, new PlatonicArguments(), Proposition.class);
	public static final Operator<Proposition> FALSE = new RealOperator<Proposition>(Constants.FALSE, new PlatonicArguments(), Proposition.class);
	public static final Operator<Proposition> EQUALITY = EqualityOperator.getInstance();
	
	private PlatonicArguments arguments;
	private Class<E> klass;
	private Integer strength;
	
	public Operator(String name, PlatonicArguments arguments, Class<E> klass, int strength) {
		this(name, arguments, klass);
		setStrength(strength);
	}
	
	public Operator(String name, PlatonicArguments arguments, Class<E> klass) {
		super(new TestArguments(), new NameStructure(name));
		this.arguments = arguments;
		this.klass = klass;
		this.strength = null;
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
	
	public void setStrength(int strength) {
		this.strength = strength;
	}
	
	public Integer getStrength() {
		return strength;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}