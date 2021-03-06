/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berkholz.helperfunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcel Berkholz
 */
public class HelperFunctions {

	private static final Logger LOG = Logger.getLogger(HelperFunctions.class.getName());

	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int getRandomInteger(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	/**
	 * Prints a HashMap to standard out and prints the number of elements at the
	 * end.
	 *
	 * @param map Map with key-values to print.
	 */
	public static void printHashMap(HashMap map) {
		Integer countOfInvalidLDAPObjects = 0;
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Boolean> entry = (Map.Entry<String, Boolean>) it.next();
			System.out.println("Key: " + entry.getKey() + " | " + entry.getValue());
			countOfInvalidLDAPObjects++;
		}
		System.out.println("Found " + countOfInvalidLDAPObjects + " key value pairs.");
	}

	/**
	 * Prints a HashMap to standard out and prints the number of elements at the
	 * end with the given value.
	 *
	 * @param map Map to print out.
	 * @param value Value that the value of the elements in the map should have,
	 * so that they are printed.
	 */
	public static void printHashMapWithValue(HashMap map, Object value) {
		Integer amountObjectsWithValue = 0;
		Integer amountObjectsWithOutValue = 0;
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Object, Object> entry;
			entry = (Map.Entry<Object, Object>) it.next();
			if (entry.getValue().equals(value)) {
				System.out.println("Key: " + entry.getKey() + " | Value: " + entry.getValue());
				amountObjectsWithValue++;
			} else {
				amountObjectsWithOutValue++;
			}
		}
		System.out.println("Found " + amountObjectsWithValue + " objects with value " + value.toString() + " and found " + amountObjectsWithOutValue + " without.");
	}

	public static void printResultSet(ResultSet rs) {
		Long n = 0L;
		try {
			while (rs.next()) {
				n++;
				// TODO: change to your needs
				System.out.println(MessageFormat.format("ID: {0} \nCAT: {1} \nFULL: {2} \nSHORT: {3} \nIDNUM: {4} \nTIME: {5}\nT2:{6}\n---------", rs.getBigDecimal("id"),
						rs.getBigDecimal("category").toBigInteger(),
						rs.getString("fullname"),
						rs.getString("shortname"),
						rs.getString("idnumber"),
						rs.getBigDecimal("timemodified"),
						rs.getString("T2")));

			}
		} catch (SQLException ex) {
			LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		System.out.println("\nCount: " + n);
	}

	/**
	 * Gets the path of a temporary directory.
	 *
	 * @return Complete path of a temporary directory.
	 */
	public static String getTempDirectory() {
		try {
			File file = File.createTempFile("tmpfile_" + getRandomInteger(1000, 9999), ".tmp");
			file.delete();
			return file.getParent() + file.separator;
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
		return null;
	}

	/**
	 * Reads in a file and returns the content of the file, each line an entry,
	 * as list.
	 *
	 * @param file File to read from the text.
	 * @return Every line of the file is stored as an entry in the list.
	 */
	public static List<String> readStringArrayFromFile(File file) {
		Charset charset = Charset.forName("UTF-8");
		List<String> resultStringArray = new ArrayList<>();

		if (file.exists()) {
			try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
				String currentLine;

				while ((currentLine = reader.readLine()) != null) {
					resultStringArray.add(currentLine);
//                    Logger.getLogger(LDAPFunctions.class.getName()).log(Level.INFO, "Adding line: {0}", currentLine);
				}
				reader.close();
			} catch (IOException e) {
				LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		return resultStringArray;
	}

	/**
	 * Method for checking the existence of a file and if it's readable.
	 *
	 * @param filename Check the existence of filename and if it is readable.
	 * @return Returns true if file exists and file is readable.
	 */
	public static Boolean checkFile(String filename) {
		if (filename != null && !filename.isEmpty()) {
			File file = new File(filename);
			return (file.exists() && file.canRead());
		}
		return false;
	}

	/**
	 * Return the platform independent representation of the users home
	 * directory.
	 *
	 * @return Directory of the users home directory as String.
	 */
	public static String getUserHomeDirectory() {
		return System.getProperty("user.home");
	}
}
