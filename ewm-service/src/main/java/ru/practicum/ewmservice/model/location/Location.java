package ru.practicum.ewmservice.model.location;

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
@Table(name = "LOCATION")
@IdClass(LocationId.class)
public class Location {
    @Id
    private Float lat;
    @Id
    private Float lon;
}
