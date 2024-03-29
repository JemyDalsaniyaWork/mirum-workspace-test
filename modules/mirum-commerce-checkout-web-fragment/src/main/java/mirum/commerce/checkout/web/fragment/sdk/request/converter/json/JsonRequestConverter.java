package mirum.commerce.checkout.web.fragment.sdk.request.converter.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.CharArrayWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mirum.commerce.checkout.web.fragment.sdk.parameter.FortKey;
import mirum.commerce.checkout.web.fragment.sdk.parameter.FortParameter;
import mirum.commerce.checkout.web.fragment.sdk.request.converter.RequestConverter;

/**
 * Implementation for @{@link RequestConverter } interface using google Gson implementation
 * *** In case you don't like to have Gson in your classpath then set any implementation for parsing and serializing using {@link FortRequestConfiguration}
 */
public final class JsonRequestConverter implements RequestConverter {

	@Override
	public FortParameter parse(String rawContent) {
		FortParameter parameter = new FortParameter();
		Gson gson = new GsonBuilder().create();

		JsonObject jsonObject = gson.fromJson(rawContent, JsonObject.class);

		for (Map.Entry<String, JsonElement> element : jsonObject.entrySet()) {
			if (element.getValue() instanceof JsonArray) {
				List<String> arrayElements = new ArrayList<>();

				element.getValue(
				).getAsJsonArray(
				).iterator(
				).forEachRemaining(
					e -> arrayElements.add(e.getAsString())
				);

				parameter.add(new FortKey(element.getKey()), arrayElements);
			}

			if (element.getValue() instanceof JsonPrimitive) {
				parameter.add(
					new FortKey(element.getKey()),
					element.getValue(
					).getAsString());
			}
		}

		return parameter;
	}

	@Override
	public String serialize(FortParameter parameter) {
		try (CharArrayWriter writer = new CharArrayWriter()) {
			Gson gson = new GsonBuilder().create();
			JsonObject jsonObject = new JsonObject();
			parameter.iterator(
				(k, v) -> {
					if (v instanceof List) {
						JsonArray element = new JsonArray();

						for (Object values : (List)v) {
							element.add(values.toString());
						}

						jsonObject.add(k.name(), element);
					}
					else {
						jsonObject.addProperty(k.name(), v.toString());
					}
				});

			return gson.toJson(jsonObject);
		}
	}

}