package prover.instruction.sentence;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.instruction.Instruction;
import prover.reader.readers.Lexer;
import prover.state.base.KnowledgeBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public abstract class Sentence<B extends KnowledgeBase, O> extends Instruction {

	private String loc;
	
	public Sentence(Lexer lex, Namespace space) throws SyntaxError {
		this(lex, space, null);
	}
	
	public Sentence(Lexer lex, Namespace space, O obj) throws SyntaxError {
		init(lex, space, obj);
		this.loc = lex.getLocation();
	}
	
	protected abstract void init(Lexer lex, Namespace space, O obj) throws SyntaxError;

	protected String location() {
		return loc;
	}
	
	public abstract Proposition execute(B base) throws LogicError;
	
}
