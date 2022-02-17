package prover.utility.utilities;

import prover.utility.Utility;

public abstract class Constants extends Utility {

	public static final String SPACE = " ";
	public static final String REGEX_EXCAPE = "\\";
	public static final String NEW_LINE = "\n";

	public static final String COMMENT_LINE = "//";
	public static final String COMMENT_START = "/*";
	public static final String COMMENT_END = "*/";

	public static final String FILE_NAME = "Main";
	public static final String PROOF_LOCATION = "proofs/";
	private static final String VALIDATE_LOCATION = "valid/";
	public static final String VALIDATE_PROOF_LOCATION = VALIDATE_LOCATION + "proofs/";
	public static final String VALIDATE_SPACE_LOCATION = VALIDATE_LOCATION + "spaces/";
	public static final String VALIDATE_BASE_LOCATION = VALIDATE_LOCATION + "bases/";
	public static final String FILE_EXTENSION = ".proof";
	public static final String FOLDER_SYSTEM_SEPARATOR = "/";
	public static final String FOLDER_LANG_SEPARATOR = ".";
	public static final String FOLDER_ALL = "*";

	public static final Pair<String, String> STRENGTH = NewCollection.pair("|", "|");
	public static final Pair<String, String> PREDICATES = NewCollection.pair("[", "]");
	public static final Pair<String, String> FUNCTIONS = NewCollection.pair("{", "}");
	public static final Pair<String, String> ELEMENTS = NewCollection.pair("(", ")");
	public static final Pair<String, String> DEFINITIONS = NewCollection.pair(":", ":");

	public static final String COMMA = ",";

	public static final Pair<String, String> PARENS = NewCollection.pair("(", ")");
	public static final Pair<String, String> BLOCKS = NewCollection.pair("{", "}");

	public static final Pair<String, String> FORALL = NewCollection.pair("[", "]");
	public static final Pair<String, String> EXISTS = NewCollection.pair("{", "}");

	public static final Pair<String, String> LISTS = NewCollection.pair("[", "]");

	public static final String JUSTIFICATION_SEPARATOR = "|";
	public static final String DECLARATION_SEPARATOR = ":";
	public static final String SUBSTITUTION_SEPARATOR = ":";

	public static final String IMPORT = "import";
	public static final String AXIOM = "axiom";
	public static final String THEOREM = "theorem";
	public static final String LEMMA = "lemma";
	public static final String TRANSPARENT = "transparent";
	public static final String OPAQUE = "opaque";
	public static final String PREDICATE = "predicate";
	public static final String FUNCTION = "function";
	public static final String ELEMENT = "element";

	public static final String FROM = "from";
	public static final String GOAL = "goal";
	public static final String FOR = "for";

	public static final String ASSUMPTION = "assumption";
	public static final String TRUTHS = "truths";
	public static final String SUBSTITUTION = "substitution";
	public static final String SORRY = "sorry";

	public static final String IFF = "<->";
	public static final String IF = "->";
	public static final String OR = "OR";
	public static final String AND = "AND";
	public static final String NOT = "NOT";

	public static final String TEST = "_";
	public static final String FALSE = "False";
	public static final String TRUE = "True";
	public static final String EQUALITY = "=";
	public static final String DISQUALITY = "!=";

}
