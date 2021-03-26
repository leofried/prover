package prover.utility.utilities;

import prover.utility.Utility;

public abstract class Constants extends Utility {
	
	public static final String FILE_LOCATION			= "proofs/";
	public static final String FILE_EXTENTION			= ".proof";
	public static final String FOLDER_SYSTEM_SEPARATOR	= "/";
	public static final String FOLDER_LANG_SEPARATOR	= ".";
	public static final String FOLDER_ALL 				= "*";
	
	public static final Pair<String, String> PARENS = NewCollection.pair("(", ")");
	public static final Pair<String, String> BLOCKS = NewCollection.pair("{", "}");
	
	public static final Pair<String, String> FORALL = NewCollection.pair("[", "]");
	public static final Pair<String, String> EXISTS = NewCollection.pair("{", "}");
	
	public static final Pair<String, String> PREDICATES	= NewCollection.pair("[", "]");
	public static final Pair<String, String> FUNCTIONS	= NewCollection.pair("{", "}");
	public static final Pair<String, String> ELEMENTS	= NewCollection.pair("(", ")");
	public static final String COMMA = ",";
	
	public static final String JUSTIFICATION_SEPARATOR	= "|";
	public static final String DECLARATION_SEPARATOR	= ":";
	
	public static final String TEST		 = "_";
	public static final String FALSE	 = "False";
	public static final String TRUE      = "True";
	public static final String EQUALITY  = "=";
	
	public static final String COMMENT_LINE		= "//";
	public static final String COMMENT_START	= "/*";
	public static final String COMMENT_END		= "*/";
	
}
