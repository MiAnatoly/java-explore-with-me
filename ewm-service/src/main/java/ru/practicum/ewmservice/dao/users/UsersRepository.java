package ru.practicum.ewmservice.dao.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.model.user.User;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {

    Page<User> findByIdIn(List<Long> ids, Pageable pageable);
}
