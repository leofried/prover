package prover.instruction.sentence.other;

import java.util.List;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.instruction.sentence.Sentence;
import prover.instruction.sentence.goal.axiom.AxiomSentence;
import prover.instruction.sentence.goal.proof.proofs.NamedLemmaSentence;
import prover.instruction.sentence.goal.proof.proofs.TheoremSentence;
import prover.reader.readers.Executer;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;

public class FileSentence extends Sentence<FileBase, Executer> {

	private ImportSentence importSentence;
	private List<Sentence<FileBase, ?>> sentences;
	private Validator valid;

	public FileSentence(Lexer lex, Namespace space, Executer exec, String fileName) throws SyntaxError {
		super(lex, space, new Validator(fileName), exec);
	}
	
	protected void compile(Lexer lex, Namespace space, Validator valid, Executer exec) throws SyntaxError {
		this.valid = valid;
		
		importSentence = new ImportSentence(lex, space, valid, exec);
		
		sentences = NewCollection.list();
		
		while(true) {
			if(lex.endOfFile())                         break;
			else if(lex.check(Constants.AXIOM)) 	    sentences.add(new AxiomSentence(lex, space, valid));			
			else if(lex.check(Constants.THEOREM))   	sentences.add(new TheoremSentence(lex, space, valid));
			else if(lex.check(Constants.LEMMA)) 	    sentences.add(new NamedLemmaSentence<FileBase>(lex, space, valid));
			else if(lex.check(Constants.OPAQUE))        sentences.add(OperatorSentence.factory(lex, space, valid, false));
			else if(lex.check(Constants.TRANSPARENT))   sentences.add(OperatorSentence.factory(lex, space, valid, true));
			else                                        throw new UnexpectedTokenError(lex);
		}
	}

	@Override
	public Proposition run(FileBase base, Validator ph) throws LogicError {
		importSentence.run(base, valid);
		
		for(Sentence<FileBase, ?> sentence : sentences) {
			sentence.run(base, valid);
		}
	
		return null;
	}
	
	public String importedSpace() {
		return importSentence.importedSpace();
	}

	public String importedBase() {
		return importSentence.importedBase();
	}

}
