package io.tvcalendar.client;

import io.tvcalendar.web.rest.dto.SerieDTO;

public interface ISerieClient {

    public SerieDTO getSerie(String title);
}
