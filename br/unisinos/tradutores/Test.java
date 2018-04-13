package br.unisinos.tradutores;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class Test {

	public static void main(String[] args) throws IOException {
		Reader r = new StringReader("/*abc*/if(x == 7);//oi");
		StreamTokenizer st = new StreamTokenizer(r);
		st.slashSlashComments(true);
		st.slashStarComments(true);
		st.eolIsSignificant(true);
		int currentToken = st.nextToken();
		while (currentToken != StreamTokenizer.TT_EOF) {
			switch (currentToken) {
			case StreamTokenizer.TT_NUMBER:
				double num = st.nval;
				System.out.println("Number found: " + num);
				break;
			case StreamTokenizer.TT_WORD:
				String word = st.sval;
				System.out.println("Word found: " + word);
				break;
			case '+':
				break;
			case '-':
				break;
			case '/':
				break;
			case '*':
				break;
			case '<': {
				int t = st.nextToken();
				switch (t) {
				case '=':
					System.out.println("<=");
					break;
				case '<':
					System.out.println("<<");
					break;
				default:
					st.pushBack();
					System.out.println("<");
					break;
				}
			}
			case '=': {
				int t = st.nextToken();
				switch (t) {
				case '=':
					System.out.println("==");
					break;
				default:
					st.pushBack();
					System.out.println("=");
					break;
				}
			}
			case '>': {
				int t = st.nextToken();
				switch (t) {
				case '=':
					System.out.println(">=");
					break;
				case '<':
					System.out.println(">>");
					break;
				default:
					st.pushBack();
					System.out.println(">");
					break;
				}
			}
			}
			currentToken = st.nextToken();
		}
	}
}