package prover.reader.readers;

import java.util.List;
import java.util.Map;

import prover.error.syntax.SyntaxError;
import prover.error.syntax.syntaxes.OperatorDeclerationError;
import prover.error.syntax.syntaxes.UnexpectedTokenError;
import prover.reader.Reader;
import prover.state.base.bases.FileBase;
import prover.state.space.Namespace;
import prover.structure.regular.converter.definition.Definition;
import prover.structure.regular.converter.operator.Operator;
import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.utilities.Constants;
import prover.utility.utilities.FileType;
import prover.utility.utilities.NewCollection;
import prover.utility.utilities.Pair;

public class Validator extends Reader {

	private String fileName;
	private boolean valid;
	private Namespace space;
	private Map<String, Definition<Proposition>> theorems;

	public Validator(String fileName) {
		this.fileName = fileName;
		valid = true;
		theorems = NewCollection.map();
	}

	public void load(Namespace space) {		
		this.space = space;
		
		try {
			Lexer lex = new Lexer(fileName, new Lexer(), FileType.VALID_SPACE);

			while(!lex.endOfFile()) {
				String theoremName = lex.nextName(true);

				Pair<Namespace, List<List<? extends Operator<?>>>> pair = Parser.readOperators(lex, space, false);
				Namespace newSpace = pair.left;

				lex.force(Constants.DECLARATION_SEPARATOR);

				String loc = lex.getLocation();
				try {
					Proposition prop = Parser.readEntity(lex, newSpace, Proposition.class);
					theorems.put(theoremName, new Definition<Proposition>((List<Operator<Proposition>>) pair.right.get(0), (List<Operator<Element>>) pair.right.get(1), NewCollection.list(), prop));
				} catch (SyntaxError e) {
					if(lex.getLocation().equals(loc)) {
						lex.nextLine();
					} else {
						valid = false;
						break;
					}
				}
			}

		} catch (SyntaxError e) {
			valid = false;
		}
	}

	public void useTheorem(String name, Definition<Proposition> theorem) {
		if(theorems.containsKey(name) & !theorem.equals(theorems.get(name))) valid = false;
	}

	public <E extends Entity<E>> void loadBase(FileBase base) {
		try {
			Lexer lex = new Lexer(fileName, new Lexer(), FileType.VALID_BASE);
			if(lex.nextInteger() == 1 && base.isEmptyModel()) valid = false;

			while(!lex.endOfFile()) {
				Class<E> klass;
				if(lex.check(Constants.PREDICATE)) klass = (Class<E>) Proposition.class;
				else if(lex.check(Constants.FUNCTION)) klass = (Class<E>) Element.class;
				else throw new UnexpectedTokenError(lex);

				Integer strength = null;

				if(lex.check(Constants.STRENGTH.left)) {
					strength = lex.nextInteger();
					lex.force(Constants.STRENGTH.right);
					if(klass == Element.class && strength < Parser.BINARY_STRENGTH) throw new OperatorDeclerationError(lex, "Cannot declare nonpositive function strength");
					if(klass == Proposition.class && strength != Parser.PREDICATE_STRENGTH) throw new OperatorDeclerationError(lex, "Cannot declare nonzero predicate strength");
				}

				String name = lex.nextName(true);

				Pair<Namespace, List<List<? extends Operator<?>>>> operators = Parser.readOperators(lex, space, true);

				lex.force(Constants.DEFINITIONS.right);
				
				E entity = Parser.readEntity(lex, operators.left, klass);
				Definition<E> definition = new Definition<E>((List<Operator<Proposition>>) operators.right.get(0), (List<Operator<Element>>)operators.right.get(1), (List<Operator<Element>>) operators.right.get(2), entity);

				boolean found = false;
				for(Operator<?> operator : base.getGlobalSugars().keySet()) {
					if(!operator.getName().equals(name)) continue;
					
					found = true;
					
					if(klass != operator.getConverterClass()) valid = false;
					if(strength != operator.getStrength()) valid = false;
					if(!definition.equals(base.getGlobalSugars().get(operator))) valid = false;

					break;
				}
				
				if(!found) valid = false;
			}
		} catch (SyntaxError e) {
			valid = false;
		}
	}

	public boolean isValid() {
		return valid;
	}

}
