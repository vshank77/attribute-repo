package org.polyglotted.attributerepo.git.common;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static org.polyglotted.attributerepo.git.common.GitConstants.CHARSET_ISO_8859_1;
import static org.polyglotted.crypto.utils.Charsets.UTF8;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import lombok.SneakyThrows;
import net.iharder.Base64;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public abstract class GitUtils {

    private static final MapJoiner joiner = Joiner.on("&").withKeyValueSeparator("=");

    @SneakyThrows
    public static String encode(final String value) {
        return URLEncoder.encode(value, CHARSET_ISO_8859_1);
    }

    @SneakyThrows
    public static String decode(final String value) {
        return URLDecoder.decode(value, CHARSET_ISO_8859_1);
    }

    public static String toBase64(final String value) {
        return Base64.encodeBytes(UTF8.getBytes(value));
    }

    @SneakyThrows
    public static String fromBase64(String content) {
        return UTF8.getString(Base64.decode(content));
    }

    public static String addParams(final Map<String, String> params) {
        if (params == null || params.isEmpty())
            return "";
        return joiner.join(new EncoderIterable(params));
    }

    @SuppressWarnings("unchecked")
    public static <V> V readJson(Reader reader, Type type) throws IOException {
        if (type == null)
            return (V) CharStreams.toString(reader);

        Gson gson = createGson(false);
        return gson.fromJson(new JsonReader(reader), type);
    }

    public static final Gson createGson(final boolean serializeNulls) {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateFormatter());
        builder.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES);
        if (serializeNulls)
            builder.serializeNulls();
        return builder.create();
    }
}
