package prover.state.base.bases;

import java.util.Set;

import prover.error.logic.logics.CannotResolveError;
import prover.error.syntax.syntaxes.DuplicateNameError;
import prover.state.base.KnowledgeBase;
import prover.structure.regular.RegularStructure;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.structure.regular.entity.proposition.binary.flippable.equality.EqualityProposition;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.PlatonicArguments;

public class TheoremBase extends KnowledgeBase {

	private String name;
	private Proposition goal;
	private Set<Proposition> truths;
	private Set<Set<Element>> equalities;
	
	public TheoremBase(KnowledgeBase base) throws DuplicateNameError {
		this(base, "", Proposition.FALSE);
	}
	
	public TheoremBase(KnowledgeBase base, String name, Proposition goal) {	
		super(base);

		truths = NewCollection.set(Proposition.TRUE);
		equalities = NewCollection.set();
		
		this.name = name;
		setGoal(goal);
		
		if(base instanceof TheoremBase) {
			truths.addAll(((TheoremBase) base).truths);
			for(Set<Element> miniset : ((TheoremBase) base).equalities) {
				Element head = miniset.iterator().next();
				for(Element element : miniset) {
					setEqual(head, element);
				}
			}
		}
	}	
	
	
	
	public void resolve(String loc) throws CannotResolveError {
		if(!goal.isTrue(this)) {
			throw new CannotResolveError(loc, name);
		}
	}

	public Proposition getGoal() {
		return goal;
	}

	public void setGoal(Proposition prop) {
		goal = prop;
		addStructureInformation(goal);
	}
	
	
	
	public Set<Proposition> getTruths() {
		return NewCollection.set(truths);
	}

	public Set<Proposition> getTruthsAndEqualities() {
		Set<Proposition> set = getTruths();
		
		for(Set<Element> miniset : equalities) {
			for(Element e : miniset) {
				for(Element f : miniset) {
					set.add(new EqualityProposition(e, f));
				}
			}
		}
		
		return set;
	}
	
	public void addTruth(Proposition prop) {
		if(prop instanceof EqualityProposition){
			setEqual(((EqualityProposition) prop).getLeft(), ((EqualityProposition) prop).getRight());
		}else {
			for(Proposition newProp : prop.deconstruct()) {
				truths.add(prop);
				if(!prop.equals(newProp)){
					addTruth(newProp);
				}
			}
		}
	}
	

	
	
	private void setEqual(Element e, Element f) {
		Set<Element> eSet = getEqualitySet(e);
		Set<Element> fSet = getEqualitySet(f);

		equalities.remove(eSet);
		equalities.remove(fSet);
		eSet.addAll(fSet);
		equalities.add(eSet);
	}

	public Set<Element> getEqualitySet(Element e){
		for(Set<Element> set : equalities) {
			if(set.contains(e)) return set;
		}
		return NewCollection.set(e);
	}
	
	
	
	
	
	protected void addOtherStructureInformation(RegularStructure<?> struct) {
		if(struct instanceof Operator
				&& ((Operator<?>) struct).getConverterClass() == Element.class
				&& ((Operator<?>) struct).getArguments().equals(new PlatonicArguments())) {
			setEqual(new Element(((Operator<Element>) struct)), new Element(((Operator<Element>) struct)));
		}
		for(Element element : struct.getAllElements()) {
			setEqual(element, element);
		}
	}
}
