package prover.reader.readers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import prover.error.syntax.SyntaxError.StringType;
import prover.error.syntax.syntaxes.NoSuchNameError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.error.syntax.syntaxes.WrongNumberOfArgumentsError;
import prover.reader.Reader;
import prover.utility.utilities.Constants;
import prover.utility.utilities.FileType;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class Lexer extends Reader {

	private static String getRegex(String[] matches) {
		String reg = new String();
		for(String match : matches) {
			reg += match + '|';
		}
		reg = reg.substring(0, reg.length() - 1);
		return reg;
	}

	private static final String REGEX = getRegex(new String[]{		
			Constants.COMMENT_LINE .replaceAll(".", Constants.REGEX_EXCAPE + Constants.REGEX_EXCAPE + "$0"),
			Constants.COMMENT_START.replaceAll(".", Constants.REGEX_EXCAPE + Constants.REGEX_EXCAPE + "$0"),
			Constants.COMMENT_END  .replaceAll(".", Constants.REGEX_EXCAPE + Constants.REGEX_EXCAPE + "$0"),

			Constants.REGEX_EXCAPE + Constants.STRENGTH   .left ,
			Constants.REGEX_EXCAPE + Constants.STRENGTH   .right,
			Constants.REGEX_EXCAPE + Constants.PREDICATES .left ,
			Constants.REGEX_EXCAPE + Constants.PREDICATES .right,
			Constants.REGEX_EXCAPE + Constants.FUNCTIONS  .left ,
			Constants.REGEX_EXCAPE + Constants.FUNCTIONS  .right,
			Constants.REGEX_EXCAPE + Constants.ELEMENTS   .left ,
			Constants.REGEX_EXCAPE + Constants.ELEMENTS   .right,
			Constants.REGEX_EXCAPE + Constants.DEFINITIONS.left ,
			Constants.REGEX_EXCAPE + Constants.DEFINITIONS.right,
			Constants.REGEX_EXCAPE + Constants.PARENS     .left ,
			Constants.REGEX_EXCAPE + Constants.PARENS     .right,
			Constants.REGEX_EXCAPE + Constants.BLOCKS     .left ,
			Constants.REGEX_EXCAPE + Constants.BLOCKS     .right,
			Constants.REGEX_EXCAPE + Constants.FORALL     .left ,
			Constants.REGEX_EXCAPE + Constants.FORALL     .right,
			Constants.REGEX_EXCAPE + Constants.EXISTS     .left ,
			Constants.REGEX_EXCAPE + Constants.EXISTS     .right,

			Constants.REGEX_EXCAPE + Constants.COMMA,
			Constants.REGEX_EXCAPE + Constants.JUSTIFICATION_SEPARATOR,
			Constants.REGEX_EXCAPE + Constants.DECLARATION_SEPARATOR,
			Constants.REGEX_EXCAPE + Constants.SUBSTITUTION_SEPARATOR,
	});

	public static final List<String> ILLEGAL_NAMES = Arrays.asList(new String[]{ 
			Constants.STRENGTH   .left ,
			Constants.STRENGTH   .right,
			Constants.PREDICATES .left ,
			Constants.PREDICATES .right,
			Constants.FUNCTIONS  .left ,
			Constants.FUNCTIONS  .right,
			Constants.ELEMENTS   .left ,
			Constants.ELEMENTS   .right,
			Constants.DEFINITIONS.left ,
			Constants.DEFINITIONS.right,
			Constants.PARENS     .left ,
			Constants.PARENS     .right,
			Constants.BLOCKS     .left ,
			Constants.BLOCKS     .right,
			Constants.FORALL     .left ,
			Constants.FORALL     .right,
			Constants.EXISTS     .left ,
			Constants.EXISTS     .right,

			Constants.COMMA,

			Constants.JUSTIFICATION_SEPARATOR,
			Constants.DECLARATION_SEPARATOR,
			Constants.SUBSTITUTION_SEPARATOR,

			Constants.IMPORT,
			Constants.AXIOM,
			Constants.THEOREM,
			Constants.LEMMA,
			Constants.TRANSPARENT,
			Constants.PREDICATE,
			Constants.FUNCTION,
			Constants.ELEMENT,

			Constants.FROM,
			Constants.GOAL,
			Constants.FOR,

			Constants.ASSUMPTION,
			Constants.TRUTHS,
			Constants.SUBSTITUTION,
			Constants.SORRY,

			Constants.IFF,
			Constants.IF,
			Constants.OR,
			Constants.AND,
			Constants.NOT,
	});


	private FileType type;
	private String fileName;
	private List<Pair<String, Integer>> tokens;
	private int dev;
	private int loc;

	public Lexer() {
		fileName = "";
		tokens = NewCollection.list();
		dev = -1;
		loc = -1;
	}

	public Lexer(String fileName, Lexer lex, FileType type) throws NoSuchNameError {
		this();
		this.type = type;
		this.fileName = fileName;

		try {
			BufferedReader br = new BufferedReader(new FileReader(type.getFile(fileName)));

			String str;
			int line = 0;
			boolean bigComment = false;
			while((str = br.readLine()) != null) {
				line++;

				for(String token : str.replaceAll(REGEX, " $0 ").split("\\s+")) {
					if(bigComment) {
						if(token.equals(Constants.COMMENT_END)) bigComment = false;
					} else {
						if(token.isEmpty()) continue;
						else if(token.equals(Constants.COMMENT_LINE)) break;
						else if(token.equals(Constants.COMMENT_START)) bigComment = true;
						else tokens.add(NewCollection.pair(token, line));
					}
				}
			}

			br.close();
		} catch (IOException e) {
			throw new NoSuchNameError(lex, this.fileName, StringType.FILE);
		}

		if(type == FileType.PROOF) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(FileType.VALID_PROOF.getFile(fileName)));

				String str;
				boolean bigComment = false;
				boolean failed = false;
				while((str = br.readLine()) != null) {
					for(String token : str.replaceAll(REGEX, " $0 ").split("\\s+")) {
						if(bigComment) {
							if(token.equals(Constants.COMMENT_END)) bigComment = false;
						} else {
							if(token.isEmpty()) continue;
							else if(token.equals(Constants.COMMENT_LINE)) break;
							else if(token.equals(Constants.COMMENT_START)) bigComment = true;
							else if(!failed && dev + 1 < tokens.size() && token.equals(tokens.get(dev + 1).left)) dev++;
							else failed = true;
						}
					}
				}

				br.close();
			}catch (IOException e) {}
		}
	}


	public String peek() {
		if(loc >= tokens.size() - 1) return "";
		else return tokens.get(loc + 1).left;
	}

	public String next() {
		String str = peek();

		loc++;
		return str;
	}

	public boolean check(String token) {
		boolean ret = peek().equals(token);
		if(ret) next();
		return ret;
	}

	public void force(String token) throws UnexpectedTokenError {
		if(!check(token)) throw new UnexpectedTokenError(this);
	}




	public String nextName(boolean toThrow) throws UnexpectedTokenError {
		String str = next();
		if(ILLEGAL_NAMES.contains(str) || (type == FileType.PROOF && str.startsWith(Constants.TEST))) {
			undo();
			if(toThrow) throw new UnexpectedTokenError(this);
			else return null;
		}
		return str;
	}

	public int nextInteger() throws UnexpectedTokenError {
		try {
			return Integer.parseInt(next());
		} catch (NumberFormatException e) {
			throw new UnexpectedTokenError(undo());
		}
	}

	public void nextBracket(int index, int arguments, Pair<String, String> parens) throws UnexpectedTokenError, WrongNumberOfArgumentsError {
		if(index == -1) {
			if(arguments != 0) force(parens.left);
			else {
				if(check(parens.left)) {
					if(!check(parens.right)) {
						undo();
					}
				}
			}
		} else {
			if(index == arguments - 1) {
				if(check(Constants.COMMA)) throw new WrongNumberOfArgumentsError(this, arguments);
				else force(parens.right);
			} else {
				if(check(parens.right)) throw new WrongNumberOfArgumentsError(this, arguments);
				else force(Constants.COMMA);			
			}
		}
	}



	public void nextLine() {
		int line = tokens.get(loc).right;
		while(!endOfFile() && tokens.get(loc + 1).right == line) {
			next();
		}
	}
	
	public boolean endOfFile() {
		return peek().equals("");
	}

	public Lexer undo() {
		loc--;
		return this;
	}

	public <T> T undo(T obj) {
		undo();
		return obj;
	}
	
	public boolean onContract() {
		return loc <= dev;
	}



	public String getFileName() {
		return fileName;
	}
	
	public String getLocation() {
		return (loc == -1 || tokens.size() == 0) ? "" : FileType.append(getFileName()) + " line " + tokens.get(Math.min(loc, tokens.size() - 1)).right;
	}

	public String toString() {
		String str = "";
		int line = tokens.get(loc).right;
		for(int i = loc+1; i<tokens.size() && line == tokens.get(i).right; i++) {
			str += tokens.get(i).left +  " ";
		}
		return str;
	}
}
