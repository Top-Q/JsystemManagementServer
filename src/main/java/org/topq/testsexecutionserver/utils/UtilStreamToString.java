package org.topq.testsexecutionserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class UtilStreamToString {
	public static String convertStreamToString(InputStream is)
			throws IOException {
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} catch (UnsupportedEncodingException e) {
				throw new UnsupportedEncodingException(
						"UnsupportedEncodingException while converting InputStream to String: "
								+ e.getMessage());
			} catch (IOException e) {
				throw new IOException(
						"IOException while converting InputStream to String: "
								+ e.getMessage());
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					throw new IOException(
							"IOException while converting InputStream to String: "
									+ e.getMessage());
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}
