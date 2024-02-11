package com.castleapi.util;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class ObjectToJsonConverter {
	private static final Jsonb JSONB = JsonbBuilder.create();

	public static String toJson(Object object) {
		return JSONB.toJson(object);
	}
}
