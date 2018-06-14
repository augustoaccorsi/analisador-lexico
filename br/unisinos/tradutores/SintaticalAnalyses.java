package br.unisinos.tradutores;

import java.util.List;

public class SintaticalAnalyses {
	private List<Token> tokens = null;
	private Token tok;
	private int index;

	public SintaticalAnalyses(List<Token> tokens) {
		this.tokens = tokens;
		this.index = 0;
		getNext();
		this.program();
		System.out.println("Success!");
	}

	private void getNext() {
		if(index == tokens.size()) {
			throwError(tok);
		}
		tok = tokens.get(index);
		index++;
	}

	private void consume(String token) { // usar o tipo do token.value
		if (tok.getValue().equals(token))
			getNext();
		else
			throwError(tok);
	}

	private void throwError(Token tok) {
		System.out.println("error on line " + tok.getLine() + "");
		System.exit(0);
	}

	private void program() {
		while (tok != null) {
			if (tok.getValue().equals("def")) {
				func();
			} else {
				var();
			}
		}
	}

	private void var() {
		type();
		if (tok.getType() == Types.Id) {
			consume(tok.getValue());
		} else {
			throwError(tok);
		}
		if (tok.getType() == Types.l_bracket) {
			consume("[");
			num(tok);
			consume("]");
		}
		consume(";");
	}

	private void num(Token token) {
		if (token.getType() == Types.num) {
			consume(token.getValue());
		} else
			throwError(token);
	}

	private void func() {
		if (tok.getValue().equals("def")) {
			consume("def");
			type();
			if (tok.getType() == Types.Id) {
				consume(tok.getValue());
			} else {
				throwError(tok);
			}
			consume("(");
			if (!tok.getValue().equals(")")) {
				paramList();
			}
			consume(")");
			block();
		}
	}

	private void paramList() {
		type();
		if (tok.getType() == Types.Id) {
			consume(tok.getValue());
		} else {
			throwError(tok);
		}
		while (tok.getValue().equals(",")) {
			consume(",");
			type();
			if (tok.getType() == Types.Id) {
				consume(tok.getValue());
			} else {
				throwError(tok);
			}
		}
	}

	private void block() {
		consume("{");
		while (tok.getType() != Types.l_braces) {
			if (isStatement() == true) {
				statement();
			} else {
				var();
			}
		}
		consume("}");
	}

	private void statement() {
		if (tok.getType() == Types.Id) {
			consume(tok.getValue());
			if (tok.getValue().equals("("))
				funcCall();
			else
				loc();
		} else {
			switch (tok.getValue()) {
			case "if":
				consume("if");
				consume("(");
				expression();
				consume(")");
				block();
				if (tok.getValue().equals("else")) {
					consume("else");
					block();
				}
				break;
			case "while":
				consume("while");
				consume("(");
				expression();
				consume(")");
				block();
				break;
			case "return":
				consume("return");
				if (tok.getValue() != ";")
					expression();
				consume(";");
				break;
			case "break":
				consume("break");
				consume(";");
				break;
			case "continue":
				consume("continue");
				consume(";");
				break;
			}
		}

	}

	private void expression() {
		if (isLiteral() == true) {
			literal();
		} else if (tok.getType() == Types.Id) {
			consume(tok.getValue());
			if (tok.getValue().equals("(")) {
				funcCall();
				if (tok.getType() == Types.Relational_Op  || tok.getType() == Types.equal) {
					consume(tok.getValue());
					expression();
				}
			} else {
				loc();
				if (tok.getType() == Types.Relational_Op || tok.getType() == Types.equal) {
					consume(tok.getValue());
					expression();
				}
			}
		} else {
			switch (tok.getType()) {
			case Arith_Op:
				consume(tok.getValue());
				expression();
				if (tok.getType() == Types.Relational_Op) {
					consume(tok.getValue());
					expression();
				}
				break;
			case l_paren:
				consume("(");
				expression();
				consume(")");
				if (tok.getType() == Types.Relational_Op) {
					consume(tok.getValue());
					expression();
				}
				break;
			default:
				throwError(tok);
			}
		}
	}

	private void argList() {
		expression();
		while (tok.getValue().equals(",")) {
			consume(",");
			expression();
		}
	}

	private void funcCall() {
		if (tok.getType() == Types.Id) {
			consume(tok.getValue());
		} else {
			throwError(tok);
		}
		consume("(");
		if (isExpression() == true)
			argList();
		consume(")");
	}

	private void type() {
		switch (tok.getValue()) {
		case "int":
			consume("int");
			break;
		case "void":
			consume("void");
			break;
		case "boolean":
			consume("boolean");
			break;
		}
	}

	private void loc() {
		if (tok.getType() == Types.Id) {
			consume(tok.getValue());
		} else {
			throwError(tok);
		}
		if (tok.getValue().equals("[")) {
			consume("[");
			num(tok);
			consume("]");
		}
	}

	private void literal() {
		switch (tok.getType()) {
		case num:
			consume(tok.getValue());
			break;
		case string_literal:
			consume(tok.getValue());
			break;
		case reserved_word:
			if(tok.getValue().equals("true") || tok.getValue().equals("false"))
				consume(tok.getValue());
			break;
		default:
			throwError(tok);
		}
	}

	private boolean isStatement() {
		if (tok.getType() == Types.Id) {
			return true;
		} else {
			switch (tok.getValue()) {
			case "if":
				return true;
			case "while":
				return true;
			case "return":
				return true;
			case "break":
				return true;
			case "continue":
				return true;
			}
		}
		return false;
	}

	private boolean isLiteral() {
		switch (tok.getType()) {
		case num:
			return true;
		case string_literal:
			return true;
		case reserved_word:
			if(tok.getValue().equals("true") || tok.getValue().equals("false"))
				return true;
		default:
			return false;
		}
	}

	private boolean isExpression() {
		if (tok.getType() == Types.Id) {
			return true;
		}
		switch (tok.getType()) {
		case Arith_Op:
			return true;
		case l_paren:
			return true;
		default:
			throwError(tok);
		}
		
		if (isLiteral() == true)
			return true;
		return false;
	}

}
