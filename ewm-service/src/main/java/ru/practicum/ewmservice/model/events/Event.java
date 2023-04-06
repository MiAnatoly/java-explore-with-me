package ru.practicum.ewmservice.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.model.categories.Category;
import ru.practicum.ewmservice.model.compilations.Compilation;
import ru.practicum.ewmservice.model.location.Location;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;
import ru.practicum.ewmservice.valide.NowBeforeDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENTS")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID", nullable = false)
    private Long id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "EVENT_DATE")
    @NowBeforeDate(hours = 2)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "INITIATOR_ID")
    private User initiator;
    @ManyToOne
    @JoinColumns({@JoinColumn(name = "LOT"), @JoinColumn(name = "LON")})
    private Location location;
    private boolean paid;
    @Column(name = "PARTICIPANT_LIMIT")
    private Integer participantLimit;
    @Column(name = "PUBLISHED_ON")
    private LocalDateTime publishedOn;
    @Column(name = "REQUEST_MODERATION")
    private boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    private String title;
    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations;
}
