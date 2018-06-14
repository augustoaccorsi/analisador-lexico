package br.unisinos.tradutores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.omg.PortableServer.ThreadPolicyOperations;

public class SintaticalAnalyses {
	private List<Token> tokens = null;
	private List<Token> bracketsToken = new ArrayList<Token>();
	private List<Token> bracketsTokenCopy = new ArrayList<Token>();

	public SintaticalAnalyses(List<Token> tokens) {
		this.tokens = tokens;
	}

	public void checkErrors() { // estorar error
		// missingBrackets();
		errorHandling();
	}

	private void errorHandling() {
		consume(tokens.remove(0));
	}

	private void consume(Token token) {
		program(token);
	}

	private void program(Token token) {
		switch (token.getType()) {
		case reserved_word: {
			if (token.getValue() == "def")
				func(tokens.remove(0));
			else if (token.getValue() == "int" || token.getValue() == "double" || token.getValue() == "bool"
					|| token.getValue() == "string" || token.getValue() == "void")
				var(tokens.remove(0));
			else
				throwError(token);
			break;
		}
		default:
			throwError(token);
		}

	}

	private void var(Token token) {
		if (token.getType() == Types.Id) {
			token = tokens.remove(0);
			if (token.getType() == Types.semicolon) {
				return;
			} else if (token.getType() == Types.l_bracket) {
				token = tokens.remove(0);
				if (token.getType() == Types.num) {
					token = tokens.remove(0);
					if (token.getType() == Types.r_bracket) {
						token = tokens.remove(0);
						if (token.getType() == Types.semicolon) {
							return;
						} else
							throwError(token);
					} else
						throwError(token);
				} else
					throwError(token);
			} else
				throwError(token);
		}
		throwError(token);
	}

	private void throwError(Token token) {
		// TODO Auto-generated method stub

	}

	private void func(Token token) {
		if (token.getValue() == "int" || token.getValue() == "double" || token.getValue() == "bool"
				|| token.getValue() == "string" || token.getValue() == "void") {
			token = tokens.remove(0);
			if (token.getType() == Types.Id) {
				token = tokens.remove(0);
				if (token.getType() == Types.l_paren) {
					token = tokens.remove(0);
					if (token.getType() == Types.r_paren) {
						token = tokens.remove(0);
						block(token);
					} else {
						token = parameters(token);
						if (token.getType() == Types.r_paren) {
							token = tokens.remove(0);
							block(token);
						} else
							throwError(token);
					}
				} else
					throwError(token);
			} else
				throwError(token);
		} else
			throwError(token);

	}

	private Token parameters(Token token) {
		while (token.getType() != Types.r_paren) {
			param(token);
			token = tokens.remove(0);
			if (token.getType() == Types.comma) {
				token = tokens.remove(0);
			}
		}
		return token;
	}

	private void param(Token token) {
		if (token.getValue() == "int" || token.getValue() == "double" || token.getValue() == "bool"
				|| token.getValue() == "string" || token.getValue() == "void") {
			token = tokens.remove(0);
			if (token.getType() != Types.Id) {
				throwError(token);
			}
		} else
			throwError(token);
	}

	// FALTA FAZER O LOOP SE FOR VVSSVSVSVSVSVSVVVVVSSS
	private void block(Token token) {
		if (token.getType() == Types.l_braces) {
			token = tokens.remove(0);
			if (token.getValue() == "int" || token.getValue() == "double" || token.getValue() == "bool"
					|| token.getValue() == "string" || token.getValue() == "void")
				while (token.getValue() == "int" || token.getValue() == "double" || token.getValue() == "bool"
						|| token.getValue() == "string" || token.getValue() == "void") {
					token = tokens.remove(0);
					var(token);
					token = tokens.remove(0);
				}
			else {
				stm(token);
			}
		}

	}

	private void stm(Token token) {
		switch (token.getType()) {
		case reserved_word: {
			if (token.getValue() == "continue") {
				token = tokens.remove(0);
				if (token.getType() == Types.semicolon)
					return;
				else
					throwError(token);
			}
			if (token.getValue() == "break") {
				token = tokens.remove(0);
				if (token.getType() == Types.semicolon)
					return;
				else
					throwError(token);
			}
			if (token.getValue() == "return") {
				token = tokens.remove(0);
				if (token.getType() == Types.semicolon || expression(token)) {
					if (expression(token)) {
						token = tokens.remove(0);
						if (token.getType() == Types.semicolon)
							return;
						else
							throwError(token);
					} else
						return;
				} else
					throwError(token);
			}
			if (token.getValue() == "while") {
				token = tokens.remove(0);
				if (token.getType() == Types.r_paren) {
					token = tokens.remove(0);
					if (expression(token)) {
						token = tokens.remove(0);
						if (token.getType() == Types.l_paren) {
							token = tokens.remove(0);
							block(token);
						} else
							throwError(token);
					} else
						throwError(token);
				} else
					throwError(token);
			}
			if (token.getValue() == "if") {
				token = tokens.remove(0);
				if (token.getType() == Types.r_paren) {
					token = tokens.remove(0);
					if (expression(token)) {
						token = tokens.remove(0);
						if (token.getType() == Types.r_paren) {
							token = tokens.remove(0);
							block(token);
							token = tokens.remove(0);
							if (token.getValue() == "else") {
								token = tokens.remove(0);
								block(token);
							}
						} else
							throwError(token);
					} else
						throwError(token);
				} else
					throwError(token);
			}
			break;
		}
		case Id: {
			if (token.getValue().equals("FuncCall")) {
				token = tokens.remove(0);
				funcCall(token);
				token = tokens.remove(0);
				if (token.getType() != Types.semicolon)
					throwError(token);
			} else if (token.getValue().equals("Loc")) {
				token = tokens.remove(0);
				loc(token);
				token = tokens.remove(0);
				if (token.getType() == Types.equal) {
					token = tokens.remove(0);
					expression(token);
					token = tokens.remove(0);
					if (token.getType() != Types.semicolon)
						throwError(token);
				} else
					throwError(token);
			} else
				throwError(token);
		}
		default:
			throwError(token);
		}
	}

	private void loc(Token token) {
		if(token.getType() == Types.Id) {
			token = tokens.remove(0);
			if(token.getType() == Types.l_paren) {
				token = tokens.remove(0);
				if(token.getType() == Types.l_bracket) {
					token = tokens.remove(0);
					expression(token);
					token = tokens.remove(0);
					if(token.getType() == Types.r_bracket) {
						token = tokens.remove(0);
					} else throwError(token);
				}
			} else
				throwError(token);
			if(token.getType() != Types.r_paren){
				throwError(token);
			}
		} else
			throwError(token);
		
	}
	
	private void lit(Token token) {
		switch(token.getValue()) {
		case "DEC":
			break;
		case "HEX":
			break;
		case "STR":
			break;
		case "true":
			break;
		case "false":
			break;
		default:
			throwError(token);
		}
	}

	private void funcCall(Token token) {
		if (token.getType() == Types.Id) {
			token = tokens.remove(0);
			if (token.getType() == Types.l_paren) {
				token = tokens.remove(0);
				argList(token); //só opcinalmete, logo se n existir ele retorna false false antes e n joga erro
				token = tokens.remove(0);
				if(token.getType() != Types.l_paren)
					throwError(token);
			} else
				throwError(token);
		} else
			throwError(token);

	}

	private void argList(Token token) {
		//if(expression(token) == false) 
		
	}

	private boolean expression(Token token) {
		// TODO Auto-generated method stub
		return false;
	}

	private void missingBrackets() { // estorar error
		for (Token token : tokens) {
			if (token.getType() == Types.l_bracket || token.getType() == Types.r_bracket
					|| token.getType() == Types.l_paren || token.getType() == Types.r_paren) {
				bracketsToken.add(token);
				bracketsTokenCopy.add(token);
			}
		}
		int position = 0;
		for (Token bracket : bracketsToken) {
			if (bracket.getType() == Types.r_bracket || bracket.getType() == Types.r_paren) {
				for (int i = (position - 1); i > 0; i--) {
					if (bracketsTokenCopy.get(i).getType() == Types.l_bracket && bracket.getType() == Types.r_bracket) {
						bracketsTokenCopy.remove(position);
						bracketsTokenCopy.remove(i);
						position = 0;
					} else if (bracketsTokenCopy.get(position).getType() == Types.l_paren
							&& bracket.getType() == Types.r_paren) {
						bracketsTokenCopy.remove(position);
						bracketsTokenCopy.remove(i);
						position = 0;
					}
					position++;
				}
			} else {
				position++;
			}
		}

		System.out.println(bracketsTokenCopy);
	}

}
