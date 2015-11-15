package io.tvcalendar.service;

import io.tvcalendar.client.ISerieClient;
import io.tvcalendar.domain.Serie;
import io.tvcalendar.repository.SerieRepository;
import io.tvcalendar.web.rest.dto.SerieDTO;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class SerieService {

    @Inject
    private SerieRepository serieRepository;

    @Resource(name="OMDBClient")
    private ISerieClient serieClient;

    public Serie save(String title) {
        SerieDTO serieDTO = serieClient.getSerie(title);
        return serieRepository.save(this.toDomain(serieDTO));
    }

    private Serie toDomain(SerieDTO serieDTO){
        Serie serie = new Serie();
        serie.setGenre(serie.getGenre());
        serie.setImdbRating(serieDTO.getImdbRating());
        serie.setName(serieDTO.getTitle());
        serie.setTitle(serieDTO.getTitle());
        serie.setPlot(serieDTO.getPlot());
        serie.setPoster(serieDTO.getPlot());
        serie.setYear(serieDTO.getYear());
        serie.setReleased(serieDTO.getReleased());
        return serie;
    }


}
