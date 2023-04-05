package ru.practicum.ewmservice.admin_service.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.categories.CategoriesRepository;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dto.categories.CategoryDto;
import ru.practicum.ewmservice.dto.categories.NewCategoryDto;
import ru.practicum.ewmservice.exception.ConflictObjectException;
import ru.practicum.ewmservice.exception.NotObjectException;
import ru.practicum.ewmservice.mapper.CategoriesMapper;
import ru.practicum.ewmservice.model.categories.Category;
import ru.practicum.ewmservice.model.events.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriesAdminServiceImpl implements CategoriesAdminService {
    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;


    @Transactional
    public CategoryDto add(NewCategoryDto newCategoryDto, HttpServletRequest request) {
        Category category = CategoriesMapper.toCategory(newCategoryDto);
        CategoryDto categoryDto = CategoriesMapper.toCategoryDto(categoriesRepository.save(category));
        log.info("add category: {} serviceAdmin", categoryDto.getName());
        return categoryDto;
    }

    @Transactional
    @Override
    public void delete(Long catId, HttpServletRequest request) {
        List<Event> events = eventsRepository.findByCategory_Id(catId, PageRequest.of(0, 1)).getContent();
        if (events.isEmpty()) {
            categoriesRepository.deleteById(catId);
            log.info("remove category: {} serviceAdmin", catId);
        } else {
            throw new ConflictObjectException("Существуют события связанные с категорией");
        }
    }

    @Transactional
    @Override
    public CategoryDto edit(Long carId, NewCategoryDto newCategoryDto, HttpServletRequest request) {
        Category categoryNew = CategoriesMapper.toCategory(carId, newCategoryDto);
        Category categoryOld = categoriesRepository.findById(carId)
                .orElseThrow(() -> new NotObjectException("категория не найдена"));
            categoryOld.setName(categoryNew.getName());
            return CategoriesMapper.toCategoryDto(categoryOld);
    }
}
