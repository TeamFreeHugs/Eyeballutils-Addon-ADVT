package com.eyeball.utils.advt.objects;

import java.util.ArrayList;

public class CrateObject implements IADVFileObject, IADVContainer {

	private String name;

	private ArrayList<IADVFileObject> objects = new ArrayList<IADVFileObject>();

	public CrateObject(String crateName) {
		this.name = crateName;
	}

	public void addObject(IADVFileObject toAdd) throws IllegalArgumentException {
		if (toAdd.equals(this))
			throw new IllegalArgumentException("Cannot add object to itself");
		objects.add(toAdd);
	}

	public void removeObject(IADVFileObject object)
			throws IllegalArgumentException {
		if (object.equals(this))
			throw new IllegalArgumentException(
					"Cannot remove object from itself");
		objects.remove(object);
	}

	@Override
	public String getName() {
		return name;
	}

	public ArrayList<IADVFileObject> getObjects() {
		return objects;
	}

}
