package org.polyglotted.attributerepo.git.common;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import lombok.SneakyThrows;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateFormatter implements JsonDeserializer<Date>, JsonSerializer<Date> {

    private ThreadLocal<DateFormat> format = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            final TimeZone timeZone = TimeZone.getTimeZone("Zulu");
            format.setTimeZone(timeZone);
            return format;
        }
    };

    @SneakyThrows
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final String value = json.getAsString();
        return format.get().parse(value);
    }

    public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(format.get().format(date));
    }
}
