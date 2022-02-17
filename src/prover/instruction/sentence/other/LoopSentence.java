package prover.instruction.sentence.other;

import java.util.List;

import prover.error.logic.LogicError;
import prover.error.syntax.SyntaxError;
import prover.instruction.sentence.Sentence;
import prover.reader.readers.Lexer;
import prover.reader.readers.Parser;
import prover.reader.readers.Validator;
import prover.state.base.bases.TheoremBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.standard.standards.TestOperator;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.PlatonicArguments;

public class LoopSentence extends Sentence<TheoremBase, Object> {

	private List<TestOperator<Element>> vars;
	private List<Element> reals;
	private List<Sentence<TheoremBase, ?>> sentences;

	public LoopSentence(Lexer lex, Namespace space, Validator valid) throws SyntaxError {
		super(lex, space, valid);
	}

	@Override
	protected void compile(Lexer lex, Namespace space, Validator valid, Object obj) throws SyntaxError {
		vars = NewCollection.list();

		if(lex.check(Constants.LISTS.left)) {
			boolean first = true;
			while(first || !lex.check(Constants.LISTS.right)) {
				if(first) first = false;
				else lex.force(Constants.COMMA);

				vars.add(new TestOperator<Element>(lex.nextName(true), PlatonicArguments.EMPTY, Element.class));
				space.addTestOperator(vars.get(vars.size() - 1), lex);
			}
		} else {
			vars.add(new TestOperator<Element>(lex.nextName(true), PlatonicArguments.EMPTY, Element.class));
			space.addTestOperator(vars.get(vars.size() - 1), lex);
		}

		lex.force(Constants.DECLARATION_SEPARATOR);

		reals = NewCollection.list();

		lex.force(Constants.LISTS.left);
		boolean first = true;
		while(first || !lex.check(Constants.LISTS.right)) {
			if(first) first = false;
			else lex.force(Constants.COMMA);

			reals.add(Parser.readEntity(lex, space, Element.class));
		}

		sentences = NewCollection.list();

		lex.force(Constants.BLOCKS.left);
		while(true) {
			if(lex.check(Constants.BLOCKS.right)) {
				break;
			} else {
				sentences.add(new StatementSentence(lex, space, valid, 1));
			}
		}

		space.removeTestOperators(vars);
	}

	@Override
	public Proposition run(TheoremBase base, Validator valid) throws LogicError {
		int[] maps = new int[vars.size()];

		while(true) {
			for(int i=0; i<maps.length; i++) {
				base.addDefinition(vars.get(i), new Definition<Element>(reals.get(maps[i])), true);
			}

			for(Sentence<TheoremBase, ?> sentence : sentences) {
				sentence.run(base, valid);
			}

			int i = 0;
			while(i < maps.length) {
				maps[i]++;
				if(maps[i] == reals.size()) {
					maps[i] = 0;
					i++;
				} else {
					break;
				}
			}

			if(i == maps.length) {
				break;
			}
		}

		return null;		
	}

}
