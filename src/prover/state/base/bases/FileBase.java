package prover.state.base.bases;

import prover.state.base.KnowledgeBase;
import prover.structure.regular.converter.operator.Operator;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;

public class FileBase extends KnowledgeBase {

	public FileBase(String fileName) {
		super(fileName);
	}
	
	@Override
	public String toString() {
		String str = (isEmptyModel() ? Integer.toString(0) : Integer.toString(1)) + Constants.NEW_LINE;
	
		for(Operator<?> operator : getGlobalSugars().keySet()) {
			str += NewCollection.chooseEntityType(operator.getConverterClass(), Constants.PREDICATE, Constants.FUNCTION)
				+ Constants.SPACE 
				+ (operator.getStrength() == null ? "" : Constants.STRENGTH.left + operator.getStrength() + Constants.STRENGTH.right + Constants.SPACE)
				+ getGlobalSugars().get(operator).toDevString(operator.getName());
		}
		
		return str;
	}
}
