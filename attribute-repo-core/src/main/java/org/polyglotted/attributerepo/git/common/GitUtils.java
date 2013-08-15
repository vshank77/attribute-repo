package org.polyglotted.attributerepo.git.common;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GitUtils {

    public static final Gson GSON = createGson(false);
    private static final MapJoiner joiner = Joiner.on("&").withKeyValueSeparator("=");

    public static String encode(final String value) {
        try {
            return URLEncoder.encode(value, GitConstants.CHARSET_ISO_8859_1);
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String decode(final String value) {
        try {
            return URLDecoder.decode(value, GitConstants.CHARSET_ISO_8859_1);
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String toBase64(final String value) {
        try {
            return Base64.encodeBase64String(value.getBytes(GitConstants.CHARSET_UTF8));

        }
        catch (Exception ex) {
            throw new RuntimeException("toBase64 failed", ex);
        }
    }

    public static String fromBase64(String content) {
        try {
            return new String(Base64.decodeBase64(content), GitConstants.CHARSET_UTF8);

        }
        catch (Exception ex) {
            throw new RuntimeException("fromBase64 failed", ex);
        }
    }

    public static String addParams(final Map<String, String> params) {
        if (params == null || params.isEmpty())
            return "";
        return joiner.join(new EncoderIterable(params));
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
