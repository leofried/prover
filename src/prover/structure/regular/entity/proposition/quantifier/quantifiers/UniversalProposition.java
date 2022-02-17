package prover.structure.regular.entity.proposition.quantifier.quantifiers;

import java.util.List;
import java.util.Set;

import prover.state.base.bases.TheoremBase;
import prover.structure.Structure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.quantifier.QuantifierProposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.OperatorDefinitionMap;
import prover.utility.utilities.TestArguments;

public final class UniversalProposition extends QuantifierProposition {

	public UniversalProposition(TestOperator<Element> instance, Proposition prop) {
		super(instance, prop);
	}

	@Override
	protected Proposition newStructure(TestArguments tests, List<Structure> subs) {
		return new UniversalProposition(tests.getElements().get(0), (Proposition) subs.get(0));
	}

	@Override
	public Proposition not() {
		return new ExistentialProposition(getInstance(), getProp().not());
	}

	@Override
	public Proposition applyElementUniversal(Element element) {
		return getProp().substituteOperators(new OperatorDefinitionMap(getInstance(), new Definition<Element>(element)));
	}

	@Override
	public Set<OperatorDefinitionMap> getSetOfMapsThatProveDeep(TheoremBase base, Set<TestOperator<?>> testOperators, Set<Proposition> targets) {
		if(base.isEmptyModel()) return NewCollection.set();
		
		Set<OperatorDefinitionMap> set = getProp().getSetOfMapsThatProve(base, NewCollection.set(testOperators, getInstance()), targets);
		for(OperatorDefinitionMap map : set) {
			map.remove(getInstance());
		}
		return set;
	}

	@Override
	protected String open() {
		return Constants.FORALL.left;
	}

	@Override
	protected String close() {
		return Constants.FORALL.right;
	}
}