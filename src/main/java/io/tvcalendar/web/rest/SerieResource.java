package io.tvcalendar.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.tvcalendar.domain.Serie;
import io.tvcalendar.repository.SerieRepository;
import io.tvcalendar.repository.search.SerieSearchRepository;
import io.tvcalendar.service.SerieService;
import io.tvcalendar.web.rest.util.HeaderUtil;
import io.tvcalendar.web.rest.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Serie.
 */
@RestController
@RequestMapping("/api")
public class SerieResource {

    private final Logger log = LoggerFactory.getLogger(SerieResource.class);

    @Inject
    private SerieRepository serieRepository;

    @Inject
    private SerieSearchRepository serieSearchRepository;

    @Inject
    private SerieService serieService;

    /**
     * POST  /series -> Create a new serie.
     */
    @RequestMapping(value = "/series",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Serie> createSerie(@Valid @RequestBody Serie serie) throws URISyntaxException {
        log.debug("REST request to save Serie : {}", serie);
        if (serie.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new serie cannot already have an ID").body(null);
        }
        Serie result = serieService.save(serie.getTitle());
        serieSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/series/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serie", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /series -> Updates an existing serie.
     */
    @RequestMapping(value = "/series",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Serie> updateSerie(@Valid @RequestBody Serie serie) throws URISyntaxException {
        log.debug("REST request to update Serie : {}", serie);
        if (serie.getId() == null) {
            return createSerie(serie);
        }
        Serie result = serieRepository.save(serie);
        serieSearchRepository.save(serie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serie", serie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /series -> get all the series.
     */
    @RequestMapping(value = "/series",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Serie>> getAllSeries(Pageable pageable)
        throws URISyntaxException {
        Page<Serie> page = serieRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/series");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /series/:id -> get the "id" serie.
     */
    @RequestMapping(value = "/series/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Serie> getSerie(@PathVariable Long id) {
        log.debug("REST request to get Serie : {}", id);
        return Optional.ofNullable(serieRepository.findOne(id))
            .map(serie -> new ResponseEntity<>(
                serie,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /series/:id -> get the "id" serie.
     */
    @RequestMapping(value = "series/preview/{title}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Serie> getSerieByTitle(@PathVariable String title) {
        log.debug("REST request to get Serie : {}", title);
        return Optional.ofNullable(serieService.getByTitle(title))
            .map(serie -> new ResponseEntity<>(
                serie,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /series/:id -> delete the "id" serie.
     */
    @RequestMapping(value = "/series/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSerie(@PathVariable Long id) {
        log.debug("REST request to delete Serie : {}", id);
        serieRepository.delete(id);
        serieSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serie", id.toString())).build();
    }

    /**
     * SEARCH  /_search/series/:query -> search for the serie corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/series/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Serie> searchSeries(@PathVariable String query) {
        return StreamSupport
            .stream(serieSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
