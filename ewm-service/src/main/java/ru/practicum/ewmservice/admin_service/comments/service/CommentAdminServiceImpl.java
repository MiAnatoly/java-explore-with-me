package ru.practicum.ewmservice.admin_service.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dao.comments.CommentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void delete(Long comId) {
        commentRepository.deleteById(comId);
        log.info("delete comment {} /CommentAdminServiceImpl", comId);
    }
}
