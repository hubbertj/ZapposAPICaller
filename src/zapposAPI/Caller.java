package zapposAPI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

//By: Jerum Lee Hubbert
//program: Zapposimg
//This file is the Business logic for reading my text file.

public class Caller {

	public Caller() {
		super();
	}

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("please provide a file name in your argument");
			System.out.println("ex. ZapposIMG skus");
			System.out.println("note: sku text must be in root directory");
			System.exit(0);
		}

		String fileName = args[0];

		if (fileName.equals("help")) {
			System.out
					.println("To use this program please provide a text file of sku #");
			System.out.println("ex. ZapposIMG sku");
			System.out.println("note: sku text must be in root directory");
			System.exit(0);
		}

		System.out.println("Starting connection...");
		System.out.println("looking for...." + fileName + ".txt");
		Vector vFile = new Caller().readTextFile(fileName);

		if (vFile.size() > 0) {
			System.out.println("These SKUs have been found");
		} else {
			System.out.println("the file has no SKU's");
		}

		for (int i = 0; i < vFile.size(); i++) {
			System.out.println(vFile.elementAt(i));
			if (vFile.size() == (i + 1)) {
				System.out
						.println("End of List ---- number for SKU's found where"
								+ " " + vFile.size());
			}
		}

		ApiConnection apc = new ApiConnection();

		// this does all my work.
		apc.connectionAndWrite(vFile);

		System.out.println("Commpleted Connection");
	}

	// used to read text file.
	private Vector readTextFile(String fileName) {
		Vector vFile = new Vector();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName + ".txt"));
		} catch (FileNotFoundException e) {
			System.out.println(fileName + ".txt" + " "
					+ "could not be found, please check the file name.");
			System.out.println("note: sku text must be in root directory");
			System.exit(0);
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				vFile.add(line.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vFile;
	}
}
