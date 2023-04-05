package ru.practicum.ewmservice.dao.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.model.compilations.Compilation;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Event, Long> {
    Page<Event> findByCategory_Id(Long id, Pageable pageable);

    Page<Event> findByInitiator(User initiator, Pageable pageable);

    List<Event> findByIdInAndState(List<Long> eventsId, State state);

    List<Event> findByIdIn(List<Long> eventsId);

    Optional<Event> findByInitiatorAndId(User initiator, Long eventId);

    List<Event> findByCompilationIn(List<Compilation> compilations);

    List<Event> findByCompilation_Id(Long id);

    @Query("select e from Event e where (?2 is null or e.category.id in ?2) " +
            "and e.paid = ?3 and (e.createdOn between ?4 and ?5)" +
            "and (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%')))")
    Page<Event> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                       LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where (?2 is null or e.category.id in ?2) " +
            "and e.paid = ?3 and (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%')))")
    Page<Event> searchWithoutDate(String text, List<Long> categories, Boolean paid, Pageable pageable);

    @Query("select e from Event e where (?1 is null or e.initiator.id in ?1) and e.state in ?2" +
            " and (?3 is null or e.category.id in ?3)")
    Page<Event> searchAdmin(List<Long> users, List<State> states, List<Long> categories, Pageable pageable);

    @Query("select e from Event e where (?1 is null or e.initiator.id in ?1) and e.state in ?2" +
            " and (?3 is null or e.category.id in ?3) and e.createdOn between ?4 and ?5")
    Page<Event> searchAdminWithDate(List<Long> users, List<State> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where (?1 is null or e.initiator.id in ?1)" +
            " and (?2 is null or e.category.id in ?2)")
    Page<Event> searchAdminAllState(List<Long> users, List<Long> categories, Pageable pageable);

    @Query("select e from Event e where (?1 is null or e.initiator.id in ?1)" +
            " and (?2 is null or e.category.id in ?2) and e.createdOn between ?3 and ?4")
    Page<Event> searchAdminWithDateAllState(List<Long> users, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

}