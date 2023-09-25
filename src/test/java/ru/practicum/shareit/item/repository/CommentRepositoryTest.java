package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DisplayName("Comment repository")
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    Item item;
    User owner;
    User booker;
    Comment comment;

    @BeforeEach
    void beforeEach() {
        owner = userRepository.save(new User(1L, "Oliver", "ooolll@mail.com"));
        booker = userRepository.save(new User(2L, "Amanda", "amama@mail.com"));
        item = itemRepository.save(new Item(1L, "Blender", "for cooking", true, owner,
                new ArrayList<>(), null));
        comment = commentRepository.save(new Comment(1L, "it is OK", item, booker, LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should find comments by item ID")
    void shouldFindCommentsByItemId() {
        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());
        Comment newComment = comments.get(0);

        assertNotNull(comments, "Comments list is null");
        assertNotNull(newComment, "Comment is null");
        assertEquals(1, comments.size(),
                "Incorrect comment list size: expected 1, actual - " + comments.size());
        assertEquals(comment.getId(), newComment.getId(),
                "Incorrect ID: expected " + comment.getId() + ", actual - " + newComment.getId());
        assertEquals(comment.getText(), newComment.getText(),
                "Incorrect TEXT: expected " + comment.getText() + ", actual - " + newComment.getText());
        assertEquals(comment.getItem().getId(), newComment.getItem().getId(),
                "Incorrect ITEM: expected " + comment.getItem().getId() + ", actual - "
                        + newComment.getItem().getId());
        assertEquals(comment.getAuthor().getId(), newComment.getAuthor().getId(),
                "Incorrect Author: expected " + comment.getAuthor().getId() + ", actual - "
                        + newComment.getAuthor().getId());
    }
}