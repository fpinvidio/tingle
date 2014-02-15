package com.plip.persistence.managers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


public class FileSystemManager {

	public static String stripExtension(String str) {
		// Handle null case specially.

		if (str == null)
			return null;

		// Get position of last '.'.

		int pos = str.lastIndexOf(".");

		// If there wasn't any '.' just return the string as is.

		if (pos == -1)
			return str;
		// Otherwise return the string, up to the dot.
		return str.substring(0, pos);
	}
	
	
	public static void checkDirectoryExists(String path){
		try {
			File destination = new File(path);
			FileUtils.forceMkdir(destination);
		} catch ( IOException e) {
			return;
		}
	}
}

