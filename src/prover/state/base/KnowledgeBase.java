package prover.state.base;

import java.util.Map;
import java.util.Set;

import prover.error.logic.logics.AmbiguousDefinitionError;
import prover.state.State;
import prover.structure.regular.RegularStructure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;

public abstract class KnowledgeBase extends State {

	private Set<Definition<Proposition>> justifications;
	private Map<Pair<Class<? extends Entity<?>>, PlatonicArguments>, Set<Definition<?>>> definitions;
	
	public KnowledgeBase() {
		justifications = NewCollection.set();
		definitions = NewCollection.map();
	}
	
	public KnowledgeBase(KnowledgeBase base) {
		this();
		justifications.addAll(base.justifications);
		
		for(Pair<Class<? extends Entity<?>>, PlatonicArguments> pair : base.definitions.keySet()) {
			for(Definition<?> definition : base.definitions.get(pair)) {
				addDefinition(definition);
			}
		}
	}
	
	
	
	
	public <E extends Entity<E>> void addJustification(Definition<Proposition> justification) {
		justifications.add(justification);
		addStructureInformation(justification);
	}
	
	public Set<Definition<Proposition>> getJustifications() {
		return justifications;
	}	
	

	
	
	private void addDefinition(Definition<?> definition) {
		Pair<Class<? extends Entity<?>>, PlatonicArguments> pair = NewCollection.pair(definition.getConverterClass(), definition.getArguments());
		if(definitions.get(pair) == null) definitions.put(pair, NewCollection.set());
		definitions.get(pair).add(definition);
	}

	public <E extends Entity<E>> Set<Definition<E>> getDefinitions(Class<E> klass, PlatonicArguments arguments, String loc) throws AmbiguousDefinitionError {
		Set<Definition<E>> set = (Set<Definition<E>>)((Set<?>) definitions.get(NewCollection.pair(klass, arguments)));
		if(set == null) throw new AmbiguousDefinitionError(loc);
		return set;
	}
	
	
	
	
	public void addStructureInformation(RegularStructure<?> struct) {
		for(Definition<?> definition : struct.getAllDefinitions()) {
			addDefinition(definition);
		}
		addOtherStructureInformation(struct);
	}

	protected abstract void addOtherStructureInformation(RegularStructure<?> struct);

}
