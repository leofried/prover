package prover.error.syntax;

import java.util.Arrays;

import prover.error.Error;
import prover.reader.readers.Lexer;

public abstract class SyntaxError extends Error {
	
	public static enum StringType {
		ELEMENT, 
		OPERATOR,
		THEOREM,
		FILE,
		PROPOSITION
	}
	
	public SyntaxError(Lexer lex, String name, String msg) {
		super("Syntax", lex.getLocation(), name + ": " + msg);
	}

	public static String surround(String str) {
		return str.isEmpty() ? "<END OF FILE>" : "'" + str + "'";
	}
	
	public static String stringify(StringType type, boolean article) {
		switch(type) {
			case ELEMENT: return articlize("element", article);
			case OPERATOR: return articlize("operator", article);
			case THEOREM: return articlize("theorem", article);
			case FILE: return articlize("file", article);
			case PROPOSITION: return articlize("proposition", article);
			default: throw new AssertionError();
		}
	}

	private static String articlize(String str, boolean article) {
		if(article) {
			str =  " " + str;
			if(Arrays.binarySearch(new char[] {'a', 'e', 'i', 'o', 'u'}, str.charAt(1)) >= 0) str = 'n' + str;
			str = 'a' + str;
		}
		return str;
	}
}
