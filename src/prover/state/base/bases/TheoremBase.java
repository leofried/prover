package prover.state.base.bases;

import java.util.List;
import java.util.Map;
import java.util.Set;

import prover.error.logic.logics.CannotResolveError;
import prover.error.syntax.syntaxes.DuplicateNameError;
import prover.reader.Reader;
import prover.state.base.KnowledgeBase;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.equality.DisqualityProposition;
import prover.structure.regular.entity.proposition.binary.equality.EqualityProposition;
import prover.structure.regular.entity.proposition.other.FalseProposition;
import prover.structure.regular.entity.proposition.other.TrueProposition;
import prover.utility.utilities.Logger;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;
import prover.utility.utilities.PlatonicArguments;

public class TheoremBase extends KnowledgeBase {

	private TheoremBase environment;
	private String theoremName;
	private Proposition goal;
	private Set<Proposition> truths;
	private Map<Element, Pair<Set<Element>, Set<Element>>> equalities;
	private Map<Pair<Class<? extends Entity<?>>, PlatonicArguments>, Set<Definition<?>>> definitions;

	public TheoremBase(KnowledgeBase base) throws DuplicateNameError {
		this(base, new String(), FalseProposition.FALSE);
	}

	public TheoremBase(KnowledgeBase base, String name, Proposition goal) {	
		super(base);

		truths = NewCollection.set(TrueProposition.TRUE);
		equalities = NewCollection.map();
		definitions = NewCollection.map();
		
		this.theoremName = name;
		setGoal(goal);

		if(base instanceof TheoremBase) {
			environment = (TheoremBase) base;
			
			truths.addAll(((TheoremBase) base).truths);
			
			for(Element e : ((TheoremBase) base).equalities.keySet()) {
				for(Element f : ((TheoremBase) base).equalities.get(e).left) {
					setEqual(e, f);
				}
				for(Element f : ((TheoremBase) base).equalities.get(e).right) {
					setDisqual(e, f);
				}
			}
		
			for(Pair<Class<? extends Entity<?>>, PlatonicArguments> pair : ((TheoremBase) base).definitions.keySet()) {
				for(Definition<?> definition : ((TheoremBase) base).definitions.get(pair)) {
					addDefinition(definition);
				}
			}
		}
	}	



	public void resolve(String loc) throws CannotResolveError {
		if(!goal.isTrue(this)) {
			if(Reader.DEBUG) {
				Logger.log(goal);
				Logger.log(truths);
				Logger.log(equalities);
			}
			throw new CannotResolveError(loc, theoremName);
		}
	}

	public Proposition getGoal() {
		return goal;
	}
	
	public List<Proposition> getGoals() {
		if(environment == null) return NewCollection.list(getGoal());
		else return NewCollection.list(getGoal(), environment.getGoals());
	}

	public void setGoal(Proposition prop) {
		setGoal(NewCollection.pair(prop, 0));
	}
	
	public void setGoal(Pair<Proposition, Integer> pair) {
		if(pair.right == 0) goal = addEntity(pair.left);
		else if(environment == null) throw new AssertionError();
		else {
			pair.right--;
			environment.setGoal(pair);
		}
	}



	public Set<Proposition> getTruths() {
		Set<Proposition> set = NewCollection.set(truths);
		
		for(Element e : equalities.keySet()) {
			for(Element f : equalities.get(e).right) {
				set.add(new DisqualityProposition(e, f));
			}
		}
		
		return set;
	}

	public Set<Proposition> getTruthsAndEqualities() {
		Set<Proposition> set = getTruths();
		
		for(Element e : equalities.keySet()) {
			for(Element f : equalities.get(e).left) {
				set.add(new EqualityProposition(e, f));
			}
		}
		
		return set;
	}

	public void addTruth(Proposition prop) {
		if(prop instanceof EqualityProposition){
			setEqual(((EqualityProposition) prop).getLeft(), ((EqualityProposition) prop).getRight());
		} else if(prop instanceof DisqualityProposition){
			setDisqual(((DisqualityProposition) prop).getLeft(), ((DisqualityProposition) prop).getRight());
		} else {
			for(Proposition newProp : prop.deconstruct()) {
				if(!prop.equals(newProp)){
					addTruth(newProp);
				} else {
					truths.add(prop);
				}
			}
		}
	}




	private void setEqual(Element e, Element f) {
		addToEqualities(e);
		addToEqualities(f);
		
		Pair<Set<Element>, Set<Element>> eWrap = equalities.get(e);
		Pair<Set<Element>, Set<Element>> fWrap = equalities.get(f);
		
		eWrap.left.addAll(fWrap.left);
		fWrap.left = eWrap.left;
		
		eWrap.right.addAll(fWrap.right);
		fWrap.right = eWrap.right;
	}
	
	private void setDisqual(Element e, Element f) {
		addToEqualities(e);
		addToEqualities(f);
		
		equalities.get(e).right.add(f);
		equalities.get(f).right.add(e);
	}
	
	private void addToEqualities(Element e) {
		if(!equalities.containsKey(e)) equalities.put(e, NewCollection.pair(NewCollection.set(e), NewCollection.set()));
	}
	
	public boolean areEqual(EqualityProposition eq) {
		if(!equalities.containsKey(eq.getLeft())) return false;
		else return equalities.get(eq.getLeft()).left.contains(eq.getRight());
	}






	public <E extends Entity<E>> Set<Definition<E>> getDefinitions(Class<E> klass, PlatonicArguments arguments, String loc) {
		Set<Definition<E>> set = (Set<Definition<E>>)((Set<?>) definitions.get(NewCollection.pair(klass, arguments)));
		Set<Definition<E>> ret = NewCollection.set();
		for(Definition<E> def : NewCollection.nullToEmpty(set)) {
			ret.add(sugar(def));
		}
		return ret;
	}
	
	public <E extends Entity<E>> E addEntity(E entity) {
		if(entity == null) entity = (E) getGoal();
		
		for(Definition<?> definition : entity.getAllDefinitions()) {
			addDefinition(definition);
		}
		
		entity = sugar(entity);
		
		for(Definition<?> definition : entity.getAllDefinitions()) {
			addDefinition(definition);
		}
	
		for(Element element : entity.getAllElements()) {
			addToEqualities(element);
		}

		return entity;
	}

	private void addDefinition(Definition<?> definition) {
		Pair<Class<? extends Entity<?>>, PlatonicArguments> pair = NewCollection.pair(definition.getConverterClass(), definition.getArguments());
		if(definitions.get(pair) == null) definitions.put(pair, NewCollection.set());
		definitions.get(pair).add(definition);
	}
}

