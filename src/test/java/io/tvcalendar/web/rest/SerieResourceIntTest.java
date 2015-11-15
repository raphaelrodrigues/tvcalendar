package io.tvcalendar.web.rest;

import io.tvcalendar.Application;
import io.tvcalendar.domain.Serie;
import io.tvcalendar.repository.SerieRepository;
import io.tvcalendar.repository.search.SerieSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SerieResource REST controller.
 *
 * @see SerieResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SerieResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_YEAR = "AAAAA";
    private static final String UPDATED_YEAR = "BBBBB";
    private static final String DEFAULT_RELEASED = "AAAAA";
    private static final String UPDATED_RELEASED = "BBBBB";
    private static final String DEFAULT_GENRE = "AAAAA";
    private static final String UPDATED_GENRE = "BBBBB";
    private static final String DEFAULT_PLOT = "AAAAA";
    private static final String UPDATED_PLOT = "BBBBB";
    private static final String DEFAULT_POSTER = "AAAAA";
    private static final String UPDATED_POSTER = "BBBBB";
    private static final String DEFAULT_IMDB_RATING = "AAAAA";
    private static final String UPDATED_IMDB_RATING = "BBBBB";
    private static final String DEFAULT_RATING = "AAAAA";
    private static final String UPDATED_RATING = "BBBBB";

    @Inject
    private SerieRepository serieRepository;

    @Inject
    private SerieSearchRepository serieSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSerieMockMvc;

    private Serie serie;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SerieResource serieResource = new SerieResource();
        ReflectionTestUtils.setField(serieResource, "serieRepository", serieRepository);
        ReflectionTestUtils.setField(serieResource, "serieSearchRepository", serieSearchRepository);
        this.restSerieMockMvc = MockMvcBuilders.standaloneSetup(serieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serie = new Serie();
        serie.setName(DEFAULT_NAME);
        serie.setTitle(DEFAULT_TITLE);
        serie.setYear(DEFAULT_YEAR);
        serie.setReleased(DEFAULT_RELEASED);
        serie.setGenre(DEFAULT_GENRE);
        serie.setPlot(DEFAULT_PLOT);
        serie.setPoster(DEFAULT_POSTER);
        serie.setImdbRating(DEFAULT_IMDB_RATING);
        serie.setRating(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void createSerie() throws Exception {
        int databaseSizeBeforeCreate = serieRepository.findAll().size();

        // Create the Serie

        restSerieMockMvc.perform(post("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serie)))
                .andExpect(status().isCreated());

        // Validate the Serie in the database
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeCreate + 1);
        Serie testSerie = series.get(series.size() - 1);
        assertThat(testSerie.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSerie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSerie.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testSerie.getReleased()).isEqualTo(DEFAULT_RELEASED);
        assertThat(testSerie.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testSerie.getPlot()).isEqualTo(DEFAULT_PLOT);
        assertThat(testSerie.getPoster()).isEqualTo(DEFAULT_POSTER);
        assertThat(testSerie.getImdbRating()).isEqualTo(DEFAULT_IMDB_RATING);
        assertThat(testSerie.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serieRepository.findAll().size();
        // set the field null
        serie.setName(null);

        // Create the Serie, which fails.

        restSerieMockMvc.perform(post("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serie)))
                .andExpect(status().isBadRequest());

        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = serieRepository.findAll().size();
        // set the field null
        serie.setTitle(null);

        // Create the Serie, which fails.

        restSerieMockMvc.perform(post("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serie)))
                .andExpect(status().isBadRequest());

        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSeries() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get all the series
        restSerieMockMvc.perform(get("/api/series"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serie.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
                .andExpect(jsonPath("$.[*].released").value(hasItem(DEFAULT_RELEASED.toString())))
                .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
                .andExpect(jsonPath("$.[*].plot").value(hasItem(DEFAULT_PLOT.toString())))
                .andExpect(jsonPath("$.[*].poster").value(hasItem(DEFAULT_POSTER.toString())))
                .andExpect(jsonPath("$.[*].imdbRating").value(hasItem(DEFAULT_IMDB_RATING.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toString())));
    }

    @Test
    @Transactional
    public void getSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get the serie
        restSerieMockMvc.perform(get("/api/series/{id}", serie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serie.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.released").value(DEFAULT_RELEASED.toString()))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE.toString()))
            .andExpect(jsonPath("$.plot").value(DEFAULT_PLOT.toString()))
            .andExpect(jsonPath("$.poster").value(DEFAULT_POSTER.toString()))
            .andExpect(jsonPath("$.imdbRating").value(DEFAULT_IMDB_RATING.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSerie() throws Exception {
        // Get the serie
        restSerieMockMvc.perform(get("/api/series/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

		int databaseSizeBeforeUpdate = serieRepository.findAll().size();

        // Update the serie
        serie.setName(UPDATED_NAME);
        serie.setTitle(UPDATED_TITLE);
        serie.setYear(UPDATED_YEAR);
        serie.setReleased(UPDATED_RELEASED);
        serie.setGenre(UPDATED_GENRE);
        serie.setPlot(UPDATED_PLOT);
        serie.setPoster(UPDATED_POSTER);
        serie.setImdbRating(UPDATED_IMDB_RATING);
        serie.setRating(UPDATED_RATING);

        restSerieMockMvc.perform(put("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serie)))
                .andExpect(status().isOk());

        // Validate the Serie in the database
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeUpdate);
        Serie testSerie = series.get(series.size() - 1);
        assertThat(testSerie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSerie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSerie.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testSerie.getReleased()).isEqualTo(UPDATED_RELEASED);
        assertThat(testSerie.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testSerie.getPlot()).isEqualTo(UPDATED_PLOT);
        assertThat(testSerie.getPoster()).isEqualTo(UPDATED_POSTER);
        assertThat(testSerie.getImdbRating()).isEqualTo(UPDATED_IMDB_RATING);
        assertThat(testSerie.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void deleteSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

		int databaseSizeBeforeDelete = serieRepository.findAll().size();

        // Get the serie
        restSerieMockMvc.perform(delete("/api/series/{id}", serie.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeDelete - 1);
    }
}
