package ru.practicum.ewmservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REQUESTS")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID")
    private User requester;
    @Enumerated(EnumType.STRING)
    private Status status;
}
