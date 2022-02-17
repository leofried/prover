package prover.state.base;

import prover.state.State;
import prover.structure.regular.RegularStructure;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.Entity;
import prover.utility.utilities.OperatorDefinitionMap;

public abstract class KnowledgeBase extends State {

	private OperatorDefinitionMap localSugars;
	private OperatorDefinitionMap globalSugars;
	private boolean emptyModel;
	
	public KnowledgeBase(String fileName) {
		super(fileName);
		localSugars = new OperatorDefinitionMap();
		globalSugars = new OperatorDefinitionMap();
		emptyModel = true;
	}
	
	public KnowledgeBase(KnowledgeBase base) {
		this(base.getName());
		importBase(base, true);
	}
	
	public void importBase(KnowledgeBase base, boolean local) {
		if(local) localSugars.putAll(base.localSugars);
		globalSugars.putAll(base.globalSugars);
		if(!base.isEmptyModel()) notEmptyModel();
	}
	
	
	
	
	public <E extends Entity<E>> void addDefinition(Operator<E> operator, Definition<E> definition, boolean local) {
		if(definition == null) return;
		
		if(local) localSugars.put(operator, definition);
		else     globalSugars.put(operator, definition);
	}
	
	public <S extends RegularStructure<S>> S sugar(S structure) {
		return structure.substituteOperators(new OperatorDefinitionMap(localSugars, globalSugars));
	}
	
	public OperatorDefinitionMap getGlobalSugars() {
		return globalSugars;
	}
	
	
	
	
	public boolean isEmptyModel() {
		return emptyModel;
	}
	
	public void notEmptyModel() {
		emptyModel = false;
	}	
}