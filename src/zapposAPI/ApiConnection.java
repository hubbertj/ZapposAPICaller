package zapposAPI;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.net.HttpURLConnection;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//By: Jerum Lee Hubbert
//program: Zapposimg
//This class is the Business logic for connection to the API server and writing files to our DIR.

public class ApiConnection {
	private File file;
	private String key = "52ddafbe3ee659bad97fcce7c53592916a6bfd73";

	public ApiConnection() {
		super();
		jinit();
	}

	private void jinit() {
		System.out.println("Making Image DIR....please wait");
		this.file = new File("images");
		Boolean suc = file.mkdirs();
		if (suc) {
			System.out.println("Image DIR was created....");
		} else {
			System.out
					.println("Image DIR either already exists or other errors occured");
		}
	}

	// returns my image URL as a string.
	private String toAString(HttpURLConnection conn) {

		BufferedReader rd = null;
		StringBuilder sb = null;
		try {
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// parses my JSON
		String JSONstring = sb.toString();
		String[] iURL = iJSONParse(JSONstring);

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < iURL.length; i++) {
			result.append(iURL[i]);
		}
		return result.toString();
	}

	// my JSON parser method
	public static String[] iJSONParse(String jSON) {
		JSONObject jresponse = null;
		try {
			jresponse = new JSONObject(jSON);
			JSONArray jproducts = jresponse.getJSONArray("product");
			String[] iURL = new String[jproducts.length()];
			for (int i = 0; i < iURL.length; i++) {
				iURL[i] = ((JSONObject) jproducts.get(i))
						.getString("defaultImageUrl");
			}
			return iURL;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void connectionAndWrite(Vector sKUs) {

		for (int i = 0; i < sKUs.size(); i++) {
			URL url = null;
			HttpURLConnection conn = null;
			String sku = (String) sKUs.elementAt(i);

			if (sku.equals("")) {
				continue;
			}

			try {
				url = new URL("http://api.zappos.com/Product/" + sku + "?key="
						+ key);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setAllowUserInteraction(false);

				if (conn.getResponseCode() != 200) {
					System.out.println("error with" + " " + sku + ","
							+ " please check the SKU #");
					continue;
				}

			} catch (MalformedURLException e2) {
				e2.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// conn must be open.
			String myURL = toAString(conn);
			conn.disconnect();

			BufferedImage bi = null;

			try {
				URL imageURL = new URL(myURL);
				bi = ImageIO.read(imageURL);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			File outputfile = new File("images\\" + sku + ".jpg");
			try {
				ImageIO.write(bi, "jpg", outputfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(sku + "# download complete!");
		}
	}
}
