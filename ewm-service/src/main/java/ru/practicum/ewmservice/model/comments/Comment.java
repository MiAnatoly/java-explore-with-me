package ru.practicum.ewmservice.model.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.valide.DateCreatedHourAfterUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENTS")
@DateCreatedHourAfterUpdate
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String description;
    private LocalDateTime created;
    private LocalDateTime updated;
    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID")
    private User requester;
}
