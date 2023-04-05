package ru.practicum.ewmservice.dao.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.model.categories.Category;


public interface CategoriesRepository extends JpaRepository<Category, Long> {

}
