package ru.practicum.ewmservice.dao.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.model.location.Location;
import ru.practicum.ewmservice.model.location.LocationId;

public interface LocationRepository extends JpaRepository<Location, LocationId> {
}
