package com.eyeball.utils.advt.objects;

public class StringObject implements IADVFileObject {
	
	private String name;
	
	private String text;
	
	public StringObject(String name) {
		this.name = name;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
