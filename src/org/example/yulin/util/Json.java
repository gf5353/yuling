package org.example.yulin.util;

import org.json.JSONArray;
import org.json.JSONException;

public class Json {

	private static Json json;

	private Json() {

	}

	public static Json getInstance() {
		if (json == null) {
			json = new Json();
		}
		return json;
	}

	public static String[] arrayToStrings(JSONArray jsonArray)
			throws JSONException {
		String[] lists = new String[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			lists[i] = (String) jsonArray.get(i);
		}
		return lists;
	}

}
