package br.unisinos.tradutores;

public class Token {
	private Types type;
	private String value;
	
	public Token(Types type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public String toString() {
		return "["+type+", "+value+"]";
	}

}
