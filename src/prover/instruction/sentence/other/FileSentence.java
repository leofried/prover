package prover.instruction.sentence.other;

import java.util.List;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.instruction.sentence.Sentence;
import prover.instruction.sentence.goal.axiom.AxiomSentence;
import prover.instruction.sentence.goal.proof.proofs.TheoremSentence;
import prover.reader.readers.Executer;
import prover.reader.readers.Lexer;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.NewCollection;

public class FileSentence extends Sentence<FileBase, Executer> {

	private List<Sentence<FileBase, ?>> sentences;

	public FileSentence(Lexer lex, Namespace space, Executer exec) throws SyntaxError {
		super(lex, space, exec);
	}
	
	protected void init(Lexer lex, Namespace space, Executer exec) throws SyntaxError {
		sentences = NewCollection.list();
		
		while(true) {
			if(lex.endOfFile()) 			break;
			else if(lex.check("import")) 	exec.readImport(lex, space);
			else if(lex.check("predicate")) sentences.add(new OperatorSentence<FileBase, Proposition>(lex, space, Proposition.class));
			else if(lex.check("function")) 	sentences.add(new OperatorSentence<FileBase, Element>(lex, space, Element.class));
			else if(lex.check("axiom")) 	sentences.add(new AxiomSentence(lex, space));			
			else if(lex.check("theorem")) 	sentences.add(new TheoremSentence<FileBase>(lex, space));
			else 							throw new UnexpectedTokenError(lex);
		}
	}

	@Override
	public Proposition execute(FileBase base) throws LogicError {
		for(Sentence<FileBase, ?> sentence : sentences) {
			sentence.execute(base);
		}
		return null;
	}

}
