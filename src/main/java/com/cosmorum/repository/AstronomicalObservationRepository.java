package com.cosmorum.repository;

import com.cosmorum.entity.AstronomicalObservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AstronomicalObservationRepository extends JpaRepository<AstronomicalObservation, Long> {
    
    @Query("SELECT o FROM AstronomicalObservation o WHERE " +
           "(:authorId IS NULL OR o.author.id = :authorId) AND " +
           "(:name IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:startTime IS NULL OR o.observationTime >= :startTime)")
    Page<AstronomicalObservation> findByFilters(
        @Param("authorId") Long authorId,
        @Param("name") String name,
        @Param("startTime") LocalDateTime startTime,
        Pageable pageable
    );
    
    @Query("SELECT o FROM AstronomicalObservation o WHERE " +
           "(:authorId IS NULL OR o.author.id = :authorId) AND " +
           "(:name IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:startTime IS NULL OR o.observationTime >= :startTime)")
    List<AstronomicalObservation> findAllByFilters(
        @Param("authorId") Long authorId,
        @Param("name") String name,
        @Param("startTime") LocalDateTime startTime
    );
}