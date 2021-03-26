package prover.reader.readers;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.error.syntax.SyntaxError.StringType;
import prover.error.syntax.syntaxes.CircularReferenceError;
import prover.error.syntax.syntaxes.NoSuchNameError;
import prover.instruction.sentence.other.FileSentence;
import prover.reader.Reader;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;

public class Executer extends Reader {

	private Map<String, Namespace> map;
	private List<FileSentence> list;

	public Executer() {
		map = NewCollection.map();
		list = NewCollection.list();
	}

	public void compile(Lexer lex, String fileName) throws SyntaxError {
		map.put(fileName, null);
		Namespace space = new Namespace();
		list.add(new FileSentence(new Lexer(fileName, lex), space, this));
		map.put(fileName, space);
	}

	public void run() throws LogicError {
		for(FileSentence file : list) {
			file.execute(new FileBase());
		}
	}

	public void readImport(Lexer lex, Namespace space) throws SyntaxError {		
		String importName = "";
		for(String str : lex.next().split("\\" + Constants.FOLDER_LANG_SEPARATOR)) {
			importName += str;
			importName += Constants.FOLDER_SYSTEM_SEPARATOR;
		}
		importName = importName.substring(0, importName.length() - 1);
		
		if(importName.endsWith(Constants.FOLDER_SYSTEM_SEPARATOR + Constants.FOLDER_ALL)) {
			importName = importName.substring(0, importName.length() - 2);
		} else {
			importName += Constants.FILE_EXTENTION;
		}

		for(String fileName : getFiles(lex, new File(Constants.FILE_LOCATION + importName), importName)) {
			if(map.containsKey(fileName)) {
				if(map.get(fileName) == null) {
					throw new CircularReferenceError(lex, fileName);
				}
			}else {
				this.compile(lex, fileName);
			}

			space.importSpace(lex, map.get(fileName), new String());
		}
	}

	private static Set<String> getFiles(Lexer lex, File file, String path) throws NoSuchNameError {		
		if(file.isDirectory()) {
			Set<String> set = NewCollection.set();
			for(File fileEntry : file.listFiles()) {
				set.addAll(getFiles(lex, fileEntry, path + Constants.FOLDER_SYSTEM_SEPARATOR + fileEntry.getName()));
			}
			return set;
		}else if(file.exists()) {
			return NewCollection.set(path);
		} else {
			throw new NoSuchNameError(lex, path, StringType.FILE);
		}
	}

}
