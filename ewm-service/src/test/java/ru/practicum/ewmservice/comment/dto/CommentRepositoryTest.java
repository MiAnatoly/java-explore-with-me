package ru.practicum.ewmservice.comment.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.ewmservice.dao.categories.CategoriesRepository;
import ru.practicum.ewmservice.dao.comments.CommentRepository;
import ru.practicum.ewmservice.dao.events.EventsRepository;
import ru.practicum.ewmservice.dao.location.LocationRepository;
import ru.practicum.ewmservice.dao.users.UsersRepository;
import ru.practicum.ewmservice.model.categories.Category;
import ru.practicum.ewmservice.model.comments.Comment;
import ru.practicum.ewmservice.model.events.Event;
import ru.practicum.ewmservice.model.location.Location;
import ru.practicum.ewmservice.model.user.User;
import ru.practicum.ewmservice.status.State;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EventsRepository eventRepository;
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;

    User user1 = new User(null, "142@mail.ru", "Bob1");
    User user2 = new User(null, "242@mail.ru", "Bob2");
    User user3 = new User(null, "342@mail.ru", "Bob3");
    User user4 = new User(null, "442@mail.ru", "Bob4");
    Location location = new Location(80.0f, 80.0f);
    Category category = new Category("вечеринка");
    Event event1 = new Event("annot по самой широкой дороге шел высокий человек",
            category, LocalDateTime.now().minusDays(2), "descr по самой широкой дороге шел высокий человек",
            LocalDateTime.now().plusDays(4), user1, location, true, 10, LocalDateTime.now(),
            true, State.PUBLISHED, "title по самой широкой дороге шел высокий человек",
            List.of(), false);
    Event event2 = new Event("annot2 по самой широкой дороге шел высокий человек",
            category, LocalDateTime.now().minusDays(2), "descr2 по самой широкой дороге шел высокий человек",
            LocalDateTime.now().plusDays(4), user2, location, true, 10, LocalDateTime.now(),
            true, State.PUBLISHED, "title2 по самой широкой дороге шел высокий человек",
            List.of(), false);
    Comment comment1 = new Comment(null, "user1 descr по самой широкой дороге шел высокий человек",
            LocalDateTime.now(), null, event1, user1);
    Comment comment2 = new Comment(null, "user2 descr по самой широкой дороге шел высокий человек",
            LocalDateTime.now(), null, event1, user2);
    Comment comment3 = new Comment(null, "user3 descr по самой широкой дороге шел высокий человек",
            LocalDateTime.now(), null, event1, user3);
    Comment comment4 = new Comment(null, "user1 descr по самой широкой дороге шел высокий человек",
            LocalDateTime.now(), null, event2, user1);
    Comment comment5 = new Comment(null, "user2 descr по самой широкой дороге шел высокий человек",
            LocalDateTime.now(), null, event2, user2);
    Comment comment6 = new Comment(null, "user3 descr по самой широкой дороге шел высокий человек",
            LocalDateTime.now(), null, event2, user2);


    @BeforeEach
    void addItem() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        locationRepository.save(location);
        categoriesRepository.save(category);
        eventRepository.save(event1);
        eventRepository.save(event2);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);
        commentRepository.save(comment6);
    }

    @Test
    void privateFindByUserTest() {
        List<Comment> comments1 = commentRepository.findByRequesterAndEvent_State(user1, State.PUBLISHED);
        List<Comment> comments2 = commentRepository.findByRequesterAndEvent_State(user2, State.PUBLISHED);
        List<Comment> comments3 = commentRepository.findByRequesterAndEvent_State(user3, State.PUBLISHED);

        Comment comment = comments3.get(0);

        assertEquals(comments1.size(), 2);
        assertEquals(comments2.size(), 3);
        assertEquals(comments3.size(), 1);
        assertNotNull(comment.getId());
    }

    @Test
    void privateFindByIdTest() {
        Comment comments1 = commentRepository.findById(this.comment1.getId()).orElse(null);
        Comment comments2 = commentRepository.findById(this.comment2.getId()).orElse(null);
        Comment comments3 = commentRepository.findById(this.comment3.getId()).orElse(null);

        assertEquals(comments1, this.comment1);
        assertEquals(comments2, this.comment2);
        assertEquals(comments3, this.comment3);

    }

    @Test
    void privateDeleteTest() {
        Comment comments1 = commentRepository.findById(this.comment1.getId()).orElse(null);
        commentRepository.deleteById(this.comment1.getId());
        Comment comments2 = commentRepository.findById(this.comment1.getId()).orElse(null);
        assertEquals(comments1, this.comment1);
        assertNull(comments2);

    }

    @Test
    void publicFindByEventTest() {
        List<Comment> comments = commentRepository
                .findByEvent_idOrderByCreated(event1.getId(), PageRequest.of(0, 10)).getContent();

        assertEquals(comments.size(), 3);

    }

}
