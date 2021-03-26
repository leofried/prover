package prover.reader.readers;

import java.io.BufferedReader;
import java.io.File;
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
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class Lexer extends Reader {

	public static String combine(String loc1, String loc2) {
		return loc1 + " -> " + loc2;
	}
	
	
	
	private static String getRegex(String[] matches) {
		String reg = "";
		for(String match : matches) {
			reg += match + '|';
		}
		reg = reg.substring(0, reg.length() - 1);
		return reg;
	}

	private static final String REGEX = getRegex(new String[]{
			"\\(", "\\)",
			"\\[", "\\]",
			"\\{", "\\}",
			"\\,",
			"\\:", "\\|",
			"\\/\\/", "\\/\\*", "\\*\\/"
	});

	private static final String[] ILLEGAL_NAMES = {
			"import", "axiom", "theorem", "lemma", "element", "predicate", "function",
			"definition", "sugar", "derivation", "symbol", "strength", "theorem",
			"assumption", "contradiction", "definition", "substitution", "truths", "sorry",
			"from", "goal", "use"
	};
	
	


	private String fileName;
	private List<Pair<String, Integer>> tokens;
	private int loc;

	public Lexer() {
		fileName = "";
		tokens = NewCollection.list();
		loc = -1;
	}

	public Lexer(String fileName, Lexer lex) throws NoSuchNameError {
		this();
		this.fileName = fileName;

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(Constants.FILE_LOCATION + fileName)));

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
		}catch (IOException e) {
			throw new NoSuchNameError(lex, fileName, StringType.FILE);
		}
	}


	
	
	public String peek() {
		if(loc == tokens.size() - 1) return "";
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
		if(str.matches(REGEX) || Arrays.asList(ILLEGAL_NAMES).contains(str)) {
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
	
	
	
	
	public String getFileName() {
		return fileName;
	}

	public String getLocation() {
		return tokens.size() == 0 ? "" : fileName + " line " + tokens.get(Math.min(loc, tokens.size() - 1)).right;
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
