package com.eyeball.utils.advt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;

import com.eyeball.utils.advt.objects.CrateObject;
import com.eyeball.utils.advt.objects.IADVFileObject;
import com.eyeball.utils.advt.objects.StringArray;
import com.eyeball.utils.advt.objects.StringObject;
import com.eyeball.utils.logging.Logger;

public class AdvancedDataWriter {

	public final File file;
	private final Logger LOGGER = new Logger("AdvancedDataWriter");
	private ArrayList<IADVFileObject> objects = new ArrayList<IADVFileObject>();

	/**
	 * 
	 * An AdvancedDataWriter Class. <br>
	 * <br>
	 * <code>
	 * new AdvancedDataWriter(new File("home/eyeball/ADVTEST"));
	 * <br>
	 * <br>
	 * </code> There is no need to add any extension.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public AdvancedDataWriter(File file) throws IOException {
		File file2 = new File(file.getAbsolutePath() + ".ADV");
		LOGGER.info(file2.getAbsolutePath());
		this.file = file2;
		if (file2.exists() && ADVDataUtils.isValid(file2)) {
			LOGGER.info("Found AdvancedDataFile " + this.file.getName());
		} else {
			LOGGER.info("Did not find AdvancedDataFile " + this.file.getName());
			LOGGER.info("Making it!");
			ADVDataUtils.createADVDFile(this.file);
		}
	}

	public void addObject(IADVFileObject object) {
		objects.add(object);
	}

	public void removeObject(IADVFileObject object) {
		objects.remove(object);
	}

	public void flush() throws IOException {
		Path p = FileSystems.getDefault().getPath(file.getAbsolutePath());
		Files.deleteIfExists(p);
		DeflaterOutputStream outputStream = new DeflaterOutputStream(
				new FileOutputStream(file, true));
		String s = getEveryThingAsXML(objects);
		String headers = ADVDataUtils.XMLTYPES.get("XMLHEADERS").replaceAll(
				"%1", s);
		outputStream.write(headers.getBytes());
		outputStream.finish();
		outputStream.flush();
		outputStream.close();
	}

	private String getEveryThingAsXML(ArrayList<IADVFileObject> objs) {
		StringBuilder sb = new StringBuilder();
		for (IADVFileObject object : objs) {
			if (object instanceof CrateObject) {
				String s = getXML((CrateObject) object);
				sb.append(s);
			} else if (object instanceof StringArray) {
				sb.append(getXMLForStringArray(object));
			} else if (object instanceof StringObject) {
				sb.append(getXMLForStringObject(sb, (StringObject) object));
			}
		}
		return sb.toString();
	}

	private String getXMLForStringObject(StringBuilder sb, StringObject o) {
		StringObject objectFinal = o;
		String xml = ADVDataUtils.XMLTYPES.get("STRINGOBJECT")
				.replaceFirst("%1", objectFinal.getName())
				.replaceFirst("%2", objectFinal.getText());
		sb.append(xml);
		return sb.toString();
	}

	private String getXML(CrateObject object) {
		StringBuilder sb = new StringBuilder();
		sb.append("<CrateObject name = " + object.getName() + ">");
		for (IADVFileObject o : object.getObjects()) {
			if (o instanceof CrateObject) {
				CrateObject objectFinal = (CrateObject) o;
				sb.append("<CrateObject name = " + objectFinal.getName() + ">");
				sb.append(getXML(objectFinal));
				sb.append("</CrateObject>");
			} else if (o instanceof StringArray) {
				sb.append(getXMLForStringArray(o));
			} else if (o instanceof StringObject) {
				StringObject objectFinal = (StringObject) o;
				String xml = ADVDataUtils.XMLTYPES.get("STRINGOBJECT")
						.replaceFirst("%1", objectFinal.getName())
						.replaceFirst("%2", objectFinal.getText());
				sb.append(xml);
			}
		}
		sb.append("</CrateObject>");
		return sb.toString();
	}

	private String getXMLForStringArray(IADVFileObject o) {
		StringBuilder tmp = new StringBuilder();
		StringArray objectFinal = (StringArray) o;
		ArrayList<StringObject> strings = objectFinal.getStrings();
		String xml = ADVDataUtils.XMLTYPES.get("STRINGARRAY");
		xml.replaceFirst("%1", objectFinal.getName());
		for (StringObject stringObject : strings) {
			String xmlObjectText = ADVDataUtils.XMLTYPES.get("STRINGOBJECT");
			xmlObjectText = xmlObjectText.replaceFirst("%1",
					stringObject.getName());
			xmlObjectText = xmlObjectText.replaceFirst("%2",
					stringObject.getText());
			tmp.append(xmlObjectText);
		}
		xml = xml.replaceFirst("%2", tmp.toString());
		return tmp.toString();
	}
}