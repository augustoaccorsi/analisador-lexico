package br.unisinos.tradutores;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) throws IOException {
		Reader reader = readFile("./br/unisinos//tradutores//InputCode.txt");
		BufferedReader br = readFile("./br/unisinos//tradutores//ReservedWords.csv");
		String line = null;
		List<String> reservedWords = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			reservedWords.add(line.replace(",", ""));
		}

		StreamTokenizer st = new StreamTokenizer(reader);
		st.slashSlashComments(true);
		st.slashStarComments(true);
		st.eolIsSignificant(true);
		int currentToken = st.nextToken();
		int scopeCount = 0;
		while (currentToken != StreamTokenizer.TT_EOF) {
			switch (currentToken) {
			case StreamTokenizer.TT_NUMBER:
				System.out.printf("[num, %s]", st.nval);
				break;
			case StreamTokenizer.TT_WORD:
				String word = st.sval;
				printTTWord(word, reservedWords, scopeCount);
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
					System.out.printf("[equal, %c]", currentToken);
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
			case '{':
				System.out.printf("[l_bracket, %c]", currentToken);
				break;
			case '}':
				System.out.printf("[r_bracket, %c]", currentToken);
				break;
			case '(':
				System.out.printf("[l_paren, %c]", currentToken);
				break;
			case ')':
				System.out.printf("[r_paren, %c]", currentToken);
				break;
			case ',':
				System.out.printf("[comma, %c]", currentToken);
				break;
			case ';':
				System.out.printf("[semicolon, %c]", currentToken);
				break;
			}
			if (currentToken == StreamTokenizer.TT_EOL) {
				System.out.println();
			}
			currentToken = st.nextToken();
		}
	}

	private static void printOperator(String operator) {
		System.out.printf("[Relational_Op, %s]", operator);
	}

	private static BufferedReader readFile(String path) throws FileNotFoundException {
		return new BufferedReader(new FileReader(path));
	}

	private static void printTTWord(String value, List<String> reservedWords, int count) {
		if (reservedWords.contains(value))
			System.out.printf("[reserved_word, %s]", value);
		else
			System.out.printf("[string_literal, %s]", value);
	}

}