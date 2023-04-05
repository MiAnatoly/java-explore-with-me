package ru.practicum.ewmservice.public_service.categories.service;

import ru.practicum.ewmservice.dto.categories.CategoryDto;

import java.util.List;

public interface CategoriesPublicService {

    List<CategoryDto> findAll(int page, int size);

    CategoryDto findById(Long catId);
}
