package br.unisinos.tradutores;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class Test {

	public static void main(String[] args) throws IOException {
		Reader r = new StringReader("/*abc*/if(x == 7 + 5) \n abc ;//oi");
		StreamTokenizer st = new StreamTokenizer(r);
		st.slashSlashComments(true);
		st.slashStarComments(true);
		st.eolIsSignificant(true);
		int currentToken = st.nextToken();
		while (currentToken != StreamTokenizer.TT_EOF) {
			switch (currentToken) {
			case StreamTokenizer.TT_NUMBER:
				System.out.printf("[num, %s]", st.nval);
				break;
			case StreamTokenizer.TT_WORD:
				String word = st.sval;
				System.out.printf("[string_literal, %s]", word);
				break;
			case '+':
			case '-':
			case '/':
			case '*':
				System.out.printf("[Arith_Op, %c]", currentToken);
				break;
			case '<': {
				int t = st.nextToken();
				switch (t) {
				case '=':
					printOperator("<=");
					break;
				case '<':
					printOperator("<<");
					break;
				default:
					st.pushBack();
					printOperator("<");
					break;
				}
			}
			case '=': {
				int t = st.nextToken();
				switch (t) {
				case '=':
					printOperator("==");
					break;
				default:
					st.pushBack();
					printOperator("=");
					break;
				}
			}
			case '>': {
				int t = st.nextToken();
				switch (t) {
				case '=':
					printOperator(">=");
					break;
				case '<':
					printOperator(">>");
					break;
				default:
					st.pushBack();
					printOperator(">");
					break;
				}
			}
			}
			if(currentToken == StreamTokenizer.TT_EOL) {
				System.out.println();
			}
			currentToken = st.nextToken();
		}
	}
	
	private static void printOperator(String operator) {
		System.out.printf("[Relational_Op, %s]", operator);
	}
}