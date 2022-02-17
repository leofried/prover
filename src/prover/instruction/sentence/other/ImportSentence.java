package prover.instruction.sentence.other;

import java.io.File;
import java.util.List;
import java.util.Set;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.error.syntax.SyntaxError.StringType;
import prover.error.syntax.syntaxes.NoSuchNameError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Executer;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.FileType;
import prover.utility.utilities.NewCollection;

public class ImportSentence extends Sentence<FileBase, Executer> {

	private Executer exec;
	private Set<String> fileNames;

	private String importedSpace;
	private String importedBase;

	public ImportSentence(Lexer lex, Namespace space, Validator valid, Executer exec) throws SyntaxError {
		super(lex, space, valid, exec);
	}

	@Override
	protected void compile(Lexer lex, Namespace space, Validator valid, Executer exec) throws SyntaxError {
		this.exec = exec;
		fileNames = NewCollection.set();

		if(lex.check(Constants.IMPORT)) {
			Set<List<String>> tree;
			if(lex.peek().equals(Constants.BLOCKS.left)) {
				tree = readTree(lex, NewCollection.list());
			} else {
				if(lex.endOfFile()) throw new UnexpectedTokenError(lex);
				tree = readTree(lex, NewCollection.list(lex.next()));
			}

			for(List<String> path : tree) {
				String str = new String();

				for(String word : path) {
					if(word.endsWith(Constants.FOLDER_LANG_SEPARATOR)) throw new NoSuchNameError(lex, "<EMPTY STRING>", StringType.FILE);
					for(String letter : word.split(Constants.REGEX_EXCAPE + Constants.FOLDER_LANG_SEPARATOR)) {
						if(Lexer.ILLEGAL_NAMES.contains(letter)) throw new NoSuchNameError(lex, letter, StringType.FILE);
						if(letter.isEmpty()) throw new NoSuchNameError(lex, "<EMPTY STRING>", StringType.FILE);
						str += letter;
						str += Constants.FOLDER_SYSTEM_SEPARATOR;
					}
				}

				str = str.substring(0, str.length() - 1);
				fileNames.addAll(fileTree(lex, str));
			}
		}

		for(String fileName : fileNames) {
			space.importSpace(lex, exec.compile(lex, fileName), false);
		}

		valid.load(space);
		importedSpace = space.toString();

	}

	private static Set<List<String>> readTree(Lexer lex, List<String> path) throws SyntaxError {
		Set<List<String>> set = NewCollection.set();

		if(lex.check(Constants.BLOCKS.left)) {
			while(!lex.check(Constants.BLOCKS.right)) {
				if(lex.endOfFile()) throw new UnexpectedTokenError(lex);
				set.addAll(readTree(lex, NewCollection.list(path, lex.next())));
			}
		} else {
			set.add(path);
		}

		return set;
	}

	private static Set<String> fileTree(Lexer lex, String path) throws SyntaxError {
		if(FileType.PROOF.getFile(path).exists()) return NewCollection.set(path);
		else {
			File file = new File(Constants.PROOF_LOCATION + path);

			if(file.isDirectory()) {
				Set<String> set = NewCollection.set();
				for(File fileEntry : file.listFiles()) {
					if(fileEntry.isDirectory()) set.addAll(fileTree(lex, path + Constants.FOLDER_SYSTEM_SEPARATOR + fileEntry.getName()));
					else if(fileEntry.getName().endsWith(Constants.FILE_EXTENSION)) set.addAll(fileTree(lex, path + Constants.FOLDER_SYSTEM_SEPARATOR + fileEntry.getName().substring(0, fileEntry.getName().length() - Constants.FILE_EXTENSION.length())));
				}
				return set;
			} else {
				throw new NoSuchNameError(lex, path, StringType.FILE);
			}
		}
	}
	
	@Override
	public Proposition run(FileBase base, Validator valid) throws LogicError {
		for(String fileName : fileNames) {
			base.importBase(exec.run(fileName), false);
		}

		valid.loadBase(base);
		importedBase = base.toString();

		return null;
	}

	public String importedSpace() {
		return importedSpace;
	}

	public String importedBase() {
		return importedBase;
	}

}
