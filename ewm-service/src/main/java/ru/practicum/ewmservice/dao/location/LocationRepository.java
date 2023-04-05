package ru.practicum.ewmservice.dao.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.model.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
