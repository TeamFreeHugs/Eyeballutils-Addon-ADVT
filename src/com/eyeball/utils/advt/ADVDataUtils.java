package com.eyeball.utils.advt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.zip.DeflaterOutputStream;

public class ADVDataUtils {

	public static final int VERSION = 1;

	public static final Hashtable<String, String> XMLTYPES = new Hashtable<String, String>();

	static {
		XMLTYPES.put("STRINGARRAY", "<StringArray name = %1 > %2 </StringArray>");
		XMLTYPES.put("STRINGOBJECT",
				"<StringObject name = %1, value = %2></StringObject>");
		XMLTYPES.put("CRATEOBJECT", "<CrateObject name = %1> %2 </CrateObject>");
		XMLTYPES.put("XMLHEADERS", "<AdvData> %1 </AdvData>");

	}

	public static boolean isValid(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String firstLine = reader.readLine();
		String secondLine = reader.readLine();

		if (firstLine.equals("--ADVFILE--") && secondLine.startsWith("TYPE=")) {
			String[] VERSION = secondLine.split("=");
			try {
				int i = Integer.parseInt(VERSION[1]);
				if (i > ADVDataUtils.VERSION) {
					reader.close();
					return false;
				}
			} catch (NumberFormatException e) {
				reader.close();
				return false;
			}
			reader.close();
			return true;
		}
		reader.close();
		return false;
	}

	public static void createADVDFile(File file) throws IOException {
		DeflaterOutputStream outputStream = new DeflaterOutputStream(
				new FileOutputStream(file, true));
		outputStream.write("--ADVFILE--\n".getBytes());
		outputStream.write(("TYPE=" + VERSION + "\n").getBytes());
		outputStream.finish();
		outputStream.flush();
		outputStream.close();
	}

}
