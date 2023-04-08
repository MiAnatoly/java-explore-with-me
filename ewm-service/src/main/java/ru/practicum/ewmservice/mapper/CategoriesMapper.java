package ru.practicum.ewmservice.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.dto.categories.CategoryDto;
import ru.practicum.ewmservice.dto.categories.NewCategoryDto;
import ru.practicum.ewmservice.model.categories.Category;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriesMapper {

    public static Category toCategory(NewCategoryDto category) {
        return new Category(
                category.getName()
        );
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static List<CategoryDto> toCategoriesDto(List<Category> category) {
        return category.stream().map(CategoriesMapper::toCategoryDto).collect(Collectors.toList());
    }
}
