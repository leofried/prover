package prover.reader.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.CircularReferenceError;
import prover.instruction.sentence.other.FileSentence;
import prover.reader.Reader;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.utility.utilities.FileType;
import prover.utility.utilities.Logger;
import prover.utility.utilities.NewCollection;

public class Executer extends Reader {

	private Map<String, Namespace> spaces;
	private Map<String, FileSentence> sentences;
	private Map<String, FileBase> bases;

	public Executer() {
		spaces = NewCollection.map();
		sentences = NewCollection.map();
		bases = NewCollection.map();
	}

	public Namespace compile(Lexer lex, String fileName) throws SyntaxError {
		if(spaces.containsKey(fileName)) {
			if(spaces.get(fileName) == null) {
				throw new CircularReferenceError(lex, FileType.append(fileName));
			}
		} else {
			spaces.put(fileName, null);
			Namespace space = new Namespace(fileName);
			Lexer lexer = new Lexer(fileName, lex, FileType.PROOF);
			sentences.put(fileName, new FileSentence(lexer, space, this, fileName));
			spaces.put(fileName, space);

			Logger.log("Complied " + FileType.append(fileName));
		}

		return spaces.get(fileName);
	}

	public FileBase run(String fileName) throws LogicError {
		if(!bases.containsKey(fileName)) {
			FileBase base = new FileBase(fileName);
			sentences.get(fileName).run(base, null);
			bases.put(fileName, base);
		}

		try {
			File devFile = FileType.VALID_PROOF.getFile(fileName);
			devFile.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(devFile);
			BufferedReader br = new BufferedReader(new FileReader(FileType.PROOF.getFile(fileName)));

			String str;
			while((str = br.readLine()) != null) {
				writer.write(str + "\n");
			}

			br.close();
			writer.close();
			
			devFile = FileType.VALID_SPACE.getFile(fileName);
			devFile.getParentFile().mkdirs();
			writer = new FileWriter(devFile);
			writer.write(sentences.get(fileName).importedSpace());
			writer.close();
			
			devFile = FileType.VALID_BASE.getFile(fileName);
			devFile.getParentFile().mkdirs();
			writer = new FileWriter(devFile);
			writer.write(sentences.get(fileName).importedBase());
			writer.close();
		} catch (IOException e) {
			throw new AssertionError("File reading issue... try running again.");
		}

		return bases.get(fileName);
	}

}
