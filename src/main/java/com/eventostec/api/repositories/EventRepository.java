package com.eventostec.api.repositories;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventAddressProjection;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventWithAddressDTO;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {



    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.address WHERE e.date > :currentDate")
    Page<Event> findUpcomingEvents(@Param("currentDate") Date currentDate, Pageable pageable);

    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.img_url AS imgUrl, e.event_url as eventUrl, e.remote AS remote, a.city as city, a.uf as uf " +
            "FROM Event e JOIN Address a ON e.id = a.event.id " +
            "WHERE (:city = '' OR a.city LIKE %:city%) " +
            "AND (:uf = '' OR a.uf LIKE %:uf%) " +
            "AND (e.date >= :startDate AND e.date <= :endDate)")
    Page<EventAddressProjection> findAllEventsByFilters(@Param("city") String city,
                                                        @Param("uf") String uf,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate,
                                                        Pageable pageable);
}
