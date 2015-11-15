package io.tvcalendar.repository;

import io.tvcalendar.domain.Serie;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Serie entity.
 */
public interface SerieRepository extends JpaRepository<Serie,Long> {

}
