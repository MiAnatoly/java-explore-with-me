package ru.practicum.ewmservice.model.compilations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPILATIONS")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPILATION_ID", nullable = false)
    private Long id;
    @Column(name = "PINNED")
    private Boolean pinned;
    @Column(name = "TITLE")
    private String title;
}
