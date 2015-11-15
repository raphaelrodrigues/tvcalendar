package io.tvcalendar.client;

import io.tvcalendar.web.rest.dto.SerieDTO;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("OMDBClient")
public class OMDBClient extends AbstractClient implements ISerieClient{

    private static final Logger LOG = LoggerFactory.getLogger(OMDBClient.class);
    public static final String BASE_URL = "http://www.omdbapi.com/";
    public static final String TITLE_PARAMETER_NAME = "t";

    /*
     * Jackson JSON configuration
     */
    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public OMDBClient() {

    }

    private URL buildUrl(String title) {
        StringBuilder sbURL = new StringBuilder(BASE_URL);

        sbURL.append("?");
        sbURL.append("t=").append(title.replace(" ", "+"));

        LOG.trace("URL = {}", sbURL.toString());
        try {
            return new URL(sbURL.toString());
        } catch (MalformedURLException ex) {
            LOG.trace("Failed to convert string to URL: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public SerieDTO getSerie(String title) {
        URL url = buildUrl(title);
        try {
            return MAPPER.readValue(get(url), SerieDTO.class);
        } catch (Exception e) {
            LOG.debug("Get Serie Error" + e);
        }
        return null;

    }

}
