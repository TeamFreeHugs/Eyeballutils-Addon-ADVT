package com.eyeball.utils.advt.objects;

import java.util.ArrayList;

public class StringArray implements IADVContainer, IADVFileObject {

	private final ArrayList<StringObject> strings = new ArrayList<StringObject>();

	private String name;

	public StringArray(String name) {
		this.name = name;
	}

	@Override
	public void addObject(IADVFileObject object)
			throws IllegalArgumentException {
		if (!(object instanceof StringObject))
			throw new IllegalArgumentException("Can only add a StringObject.");
		strings.add((StringObject) object);
	}

	@Override
	public void removeObject(IADVFileObject object)
			throws IllegalArgumentException {
		if (!(object instanceof StringObject))
			throw new IllegalArgumentException("Can only add a StringObject.");
		strings.remove(object);
	}

	public ArrayList<StringObject> getStrings() {
		return strings;
	}

	@Override
	public String getName() {
		return name;
	}

}
