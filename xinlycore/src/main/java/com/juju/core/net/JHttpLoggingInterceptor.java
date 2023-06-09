package com.juju.core.net;


import android.text.TextUtils;
import com.juju.core.log.DLog;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiangyao on 2019/8/20.
 */
public class JHttpLoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1 (3-byte body)
         *
         * <-- HTTP/1.1 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting HTTP/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
         *
         * <-- HTTP/1.1 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        void log(boolean isSuccess, String... message);

        /**
         * A {@link Logger} defaults output appropriate for the current platform.
         */
        Logger DEFAULT = (isSuccess, message) -> {
            // Platform.get().log(message);
            /**
             * 使用自定义Log 输出
             */
            if (isSuccess) {
                DLog.i((Object[]) message);
                return;
            }

            /**
             * 失败的请求不需要输出，由上层输出
             */
            DLog.e((Object) message);
        };
    }

    public JHttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public JHttpLoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    private volatile Level level = Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public JHttpLoggingInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        logger.log(response.isSuccessful(), getHttpLog(request, response, tookMs));

        return response;
    }

    public static String[] getHttpLog(Request request, Response response, long tookMs) throws IOException {

        Charset charset = UTF8;

        /**
         * header
         */
        StringBuilder headersStringBuilder = new StringBuilder();
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            if(i != 0) {
                headersStringBuilder.append("\n");
            }
            headersStringBuilder.append(headers.name(i) + ": " + headers.value(i));
        }
        headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            if(!TextUtils.isEmpty(headersStringBuilder.toString())) {
                headersStringBuilder.append("\n");
            }
            headersStringBuilder.append(headers.name(i) + ": " + headers.value(i));
        }


        /**
         * request body
         */
        Buffer bufferRequest = new Buffer();
        if (request.body() != null)
            request.body().writeTo(bufferRequest);
        String requestBody = bufferRequest.clone().readString(charset);

        /**
         * response body
         */
        ResponseBody responseBody = response.body();
        assert responseBody != null;
        long contentLength = responseBody.contentLength();
        Buffer buffer = null;
        try{
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            buffer = source.buffer();
        } catch (Exception e) {
        }

        List<String> stringList = new ArrayList<>();
        stringList.add(response.code() + " --> (" + tookMs + "ms" + (buffer != null ? "," + buffer.size() + "byte" : "") + ')');
        if(!TextUtils.isEmpty(response.message())) {
            stringList.add(response.message());
        }
        stringList.add("" + response.request().url());
        if(!TextUtils.isEmpty(headersStringBuilder)) {
            stringList.add(headersStringBuilder.toString());
        }
        if(!TextUtils.isEmpty(requestBody)) {
            stringList.add(requestBody);
        }
        if(buffer != null) {
            stringList.add(buffer.clone().readString(charset));
        }

        return stringList.toArray(new String[0]);
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }
}