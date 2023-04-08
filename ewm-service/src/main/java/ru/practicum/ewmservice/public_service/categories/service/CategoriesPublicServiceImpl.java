package ru.practicum.ewmservice.public_service.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.categories.CategoriesRepository;
import ru.practicum.ewmservice.dto.categories.CategoryDto;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.mapper.CategoriesMapper;
import ru.practicum.ewmservice.model.categories.Category;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoriesPublicServiceImpl implements CategoriesPublicService {
    private final CategoriesRepository repository;

    @Override
    public List<CategoryDto> findAll(int page, int size) {
        List<Category> categories = repository.findAll(PageRequest.of(page, size)).getContent();
        log.info("get category size:{} servicePublic", categories.size());
        return CategoriesMapper.toCategoriesDto(categories);
    }

    @Override
    public CategoryDto findById(Long catId) {
        Category category = repository.findById(catId).orElseThrow(() -> new NotObjectException("нет категории"));
        log.info("get category name:{} servicePublic", category.getName());
        return CategoriesMapper.toCategoryDto(category);
    }
}
