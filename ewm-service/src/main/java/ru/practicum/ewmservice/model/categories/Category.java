package ru.practicum.ewmservice.model.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.model.events.AbstractEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CATEGORIES")
public class Category extends AbstractEntity {
    private String name;
}
