package io.tvcalendar.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.yamj.api.common.http.DigestedResponse;
import org.yamj.api.common.http.DigestedResponseReader;

public class AbstractClient {

    private static HttpClient httpClient;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Charset CHARSET = Charset.forName(DEFAULT_CHARSET);

    public AbstractClient() {
        super();
        this.httpClient = HttpClientBuilder.create().build();
    }

    public static String get(URL url) throws Exception {
        try {
            HttpGet httpGet = new HttpGet(url.toURI());
            httpGet.addHeader("accept", "application/json");

            final DigestedResponse response = DigestedResponseReader.requestContent(httpClient, httpGet, CHARSET);

            if (response.getStatusCode() == 0) {
                throw new Exception("Error retrieving URL" + url);
            } else if (response.getStatusCode() >= 500) {
                throw new Exception();
            } else if (response.getStatusCode() >= 300) {
                throw new Exception("404");
            }

            return response.getContent();
        } catch (URISyntaxException ex) {
            throw new Exception("Invalid URL" + url);
        } catch (IOException ex) {
            throw new Exception("Error retrieving URL "+  url);
        }
    }
}
