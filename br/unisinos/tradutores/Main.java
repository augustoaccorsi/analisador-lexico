package br.unisinos.tradutores;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	static int identifierCount = 0;
	static Map<String, Integer> identifiers = new HashMap<String, Integer>();

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
		st.ordinaryChar('/');
		int currentToken = st.nextToken();
		while (currentToken != StreamTokenizer.TT_EOF) {
			switch (currentToken) {
				case StreamTokenizer.TT_NUMBER:
					System.out.printf("[num, %s]", st.nval);
					break;
				case StreamTokenizer.TT_WORD:
					String word = st.sval;
					if(reservedWords.contains(word))
						printReservedWord(word);
					else
						printIdentifier(word);
					break;
				case '+':
				case '-':
				case '/':					
				case '*':
					System.out.printf("[Arith_Op, %c]", currentToken);
					break;
				case '{':
					printOtherCharacters("l_bracket", currentToken);
					break;
				case '}':
					printOtherCharacters("r_bracket", currentToken);
					break;
				case '(':
					printOtherCharacters("l_paren", currentToken);
					break;
				case ')':
					printOtherCharacters("r_paren", currentToken);
					break;
				case ',':
					printOtherCharacters("comma", currentToken);
					break;
				case ';':
					printOtherCharacters("semicolon", currentToken);
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
					break;
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
					break;
				}
				case '>': {
					int t = st.nextToken();
					switch (t) {
					case '=':
						printOperator(">=");
						break;
					case '>':
						printOperator(">>");
						break;
					default:
						st.pushBack();
						printOperator(">");
						break;
					}
					break;
				}
				case '!': {
					int t = st.nextToken();
					switch (t) {
					case '=':
						printOperator("!=");
						break;
					default:
						st.pushBack();
						break;
					}
					break;
				}
				case '&': {
					int t = st.nextToken();
					switch (t) {
					case '&':
						printOperator("&&");
						break;
					default:
						st.pushBack();
						printOperator("&");
						break;
					}
				}
				case '|': {
					int t = st.nextToken();
					switch (t) {
					case '|':
						printOperator("||");
						break;
					default:
						st.pushBack();
						break;
					}
					break;
				}
				case '"': {
					printStringLiteral(st.sval);
					break;
				}
			}
			if (currentToken == StreamTokenizer.TT_EOL) {
//				System.out.print("\n" + st.lineno() + " -> ");
				System.out.println();
			}
			currentToken = st.nextToken();
		}
	}

	private static void printIdentifier(String word) {
		if(identifiers.get(word) != null) {
			System.out.printf("[Id, %s]", identifiers.get(word));
		} else {
			identifierCount++;
			identifiers.put(word, identifierCount);
			System.out.printf("[Id, %s]", identifierCount);
		}
	}

	private static void printOperator(String operator) {
		if (operator.equals("&&") || operator.equals("||"))
			System.out.printf("[logic_op, %s]", operator);
		else
			System.out.printf("[Relational_Op, %s]", operator);
	}

	private static BufferedReader readFile(String path) throws FileNotFoundException {
		return new BufferedReader(new FileReader(path));
	}

	private static void printStringLiteral(String value) {
		System.out.printf("[string_literal, %s]", value);
	}

	private static void printReservedWord(String value) {
		System.out.printf("[reserved_word, %s]", value);
	}

	private static void printOtherCharacters(String token, int value) {
		System.out.printf("[" + token + ", %c]", value);
	}

}