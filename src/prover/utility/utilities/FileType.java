package prover.utility.utilities;

import java.io.File;

public enum FileType {
	PROOF, VALID_PROOF, VALID_SPACE, VALID_BASE;
	
	public File getFile(String fileName) {
		String loc;
		
		switch(this) {
		case PROOF:
			loc = Constants.PROOF_LOCATION;
			break;
		case VALID_PROOF:
			loc = Constants.VALIDATE_PROOF_LOCATION;
			break;
		case VALID_SPACE:
			loc = Constants.VALIDATE_SPACE_LOCATION;
			break;
		case VALID_BASE:
			loc = Constants.VALIDATE_BASE_LOCATION;
			break;
		default:
			throw new AssertionError();
		}
		
		return new File(loc + append(fileName));
	}

	public static String append(String fileName) {
		return fileName + Constants.FILE_EXTENSION;
	}
}
