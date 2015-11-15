package io.tvcalendar.repository.search;

import io.tvcalendar.domain.Serie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Serie entity.
 */
public interface SerieSearchRepository extends ElasticsearchRepository<Serie, Long> {
}
