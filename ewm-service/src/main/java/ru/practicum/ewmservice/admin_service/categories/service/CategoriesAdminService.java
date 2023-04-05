package ru.practicum.ewmservice.admin_service.categories.service;

import ru.practicum.ewmservice.dto.categories.CategoryDto;
import ru.practicum.ewmservice.dto.categories.NewCategoryDto;

import javax.servlet.http.HttpServletRequest;

public interface CategoriesAdminService {

     CategoryDto add(NewCategoryDto categoryDto, HttpServletRequest request);

     void delete(Long catId, HttpServletRequest request);

     CategoryDto edit(Long carId, NewCategoryDto categoryDto, HttpServletRequest request);
}
