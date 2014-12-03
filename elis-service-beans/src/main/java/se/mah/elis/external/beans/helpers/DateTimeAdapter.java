package se.mah.elis.external.beans.helpers;

import java.lang.reflect.Type;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This class provides methods for marshaling/unmarshaling ojects of Joda's
 * DateTime class for either XML or JSON documents.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
@XmlTransient
public class DateTimeAdapter extends XmlAdapter<String, DateTime>
	implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
	
	/**
	 * Unmarshals a string into a DateTime object.
	 * 
	 * @param dateTime The string to parse.
	 * @return A DateTime object.
	 * @since 1.0
	 */
	@Override
	public DateTime unmarshal(String dateTime) throws Exception {
		return new DateTime(dateTime);
	}
	
	/**
	 * Marshals a DateTime object into a string.
	 * 
	 * @param dateTime The object to marshal.
	 * @return A String object.
	 * @since 1.0
	 */
	@Override
	public String marshal(DateTime dateTime) throws Exception {
		return dateTime.toString();
	}

	/**
	 * Deserializes a string into a DateTime object.
	 * 
	 * @param json The string to parse.
	 * @param typeOfT The type to deserialize into.
	 * @param context A JsonDeserializationContext context.
	 * @return A DateTime object.
	 * @since 1.0
	 */
	@Override
	public DateTime deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return new DateTime(json.getAsJsonPrimitive().getAsString());
	}

	/**
	 * Serializes a DateTime object into a string.
	 * 
	 * @param dateTime The object to serialize.
	 * @param typeOfT The type to serialize from.
	 * @param context A JsonDeserializationContext context.
	 * @return A JsonElement object.
	 * @since 1.0
	 */
	@Override
	public JsonElement serialize(DateTime src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}
	
	
}
