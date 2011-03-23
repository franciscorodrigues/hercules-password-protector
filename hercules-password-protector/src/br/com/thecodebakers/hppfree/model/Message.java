package br.com.thecodebakers.hppfree.model;

public class Message {
	private int code;
	private String text;
	
	public int getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public Message(int code, String text) {
		super();
		this.code = code;
		this.text = text;
	}
}
