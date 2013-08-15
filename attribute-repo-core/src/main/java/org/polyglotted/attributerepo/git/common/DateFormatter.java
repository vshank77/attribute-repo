package org.polyglotted.attributerepo.git.common;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateFormatter implements JsonDeserializer<Date>, JsonSerializer<Date> {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private final DateFormat format;

    public DateFormatter() {
        format = new SimpleDateFormat(DATE_FORMAT);
        final TimeZone timeZone = TimeZone.getTimeZone("Zulu");
        format.setTimeZone(timeZone);
    }

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final String value = json.getAsString();
        try {
            synchronized (format) {
                return format.parse(value);
            }
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }

    public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
        String formatted;
        synchronized (format) {
            formatted = format.format(date);
        }
        return new JsonPrimitive(formatted);
    }
}
