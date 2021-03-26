package prover.state.base.bases;

import prover.state.base.KnowledgeBase;
import prover.structure.regular.RegularStructure;

public class FileBase extends KnowledgeBase {
	
	public FileBase() {
		super();
	}

	protected void addOtherStructureInformation(RegularStructure<?> struct) {
		return;
	}
}
