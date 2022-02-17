package prover.instruction.sentence;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.instruction.Instruction;
import prover.reader.readers.Lexer;
import prover.reader.readers.Validator;
import prover.state.base.KnowledgeBase;
import prover.state.space.Namespace;
import prover.structure.regular.entity.proposition.Proposition;

public abstract class Sentence<B extends KnowledgeBase, O> extends Instruction {

	private String loc;
	
	public Sentence(Lexer lex, Namespace space, Validator valid) throws SyntaxError {
		this(lex, space, valid, null);
	}
	
	public Sentence(Lexer lex, Namespace space, Validator valid, O obj) throws SyntaxError {
		compile(lex, space, valid, obj);
		this.loc = lex.getLocation();
	}
	
	protected String location() {
		return loc;
	}
	
	protected abstract void compile(Lexer lex, Namespace space, Validator valid, O obj) throws SyntaxError;
	
	public abstract Proposition run(B base, Validator valid) throws LogicError;
	
}
