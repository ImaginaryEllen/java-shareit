package ru.practicum.shareit.item.dto.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Comment mapper")
class CommentMapperTest {
    User owner;
    User requestor;
    Item item;
    Comment comment;
    CommentDto commentDto;

    @BeforeEach
    void beforeEach() {
        owner = new User(11L, "Rick", "ricky@mail.com");
        requestor = new User(22L, "Alice", "lisalisa@mail.com");
        item = new Item(33L, "Paints", "watercolour", true, owner,
                new ArrayList<>(), null);
        comment = new Comment(44L, "not bad", item, requestor,
                LocalDateTime.of(2023, Month.JANUARY, 11, 11, 11, 11));
        commentDto = new CommentDto(55L, "perfect", "Alice",
                LocalDateTime.of(2023, Month.MAY, 10, 10, 10, 10));
    }

    @Test
    @DisplayName("should mapping comment -> commentDto")
    void shouldToCommentDto() {
        CommentDto dto = CommentMapper.toCommentDto(comment);

        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(comment.getId()));
        assertThat(dto.getText(), equalTo(comment.getText()));
        assertThat(dto.getCreated(), equalTo(comment.getCreated()));
        assertThat(dto.getAuthorName(), equalTo(comment.getAuthor().getName()));
    }

    @Test
    @DisplayName("should mapping commentDto -> comment")
    void shouldToComment() {
        Comment newComment = CommentMapper.toComment(commentDto, item, requestor, commentDto.getCreated());

        assertNotNull(newComment, "New comment is null");
        assertThat(newComment.getId(), equalTo(commentDto.getId()));
        assertThat(newComment.getText(), equalTo(commentDto.getText()));
        assertThat(newComment.getCreated(), equalTo(commentDto.getCreated()));
        assertThat(newComment.getAuthor().getName(), equalTo(commentDto.getAuthorName()));
    }

    @Test
    @DisplayName("should mapping comment list -> commentDto list")
    void shouldToCommentDtoList() {
        List<Comment> comments = List.of(comment);
        List<CommentDto> commentDtoList = CommentMapper.toCommentDtoList(comments);
        CommentDto dto = commentDtoList.get(0);

        assertEquals(comments.size(), commentDtoList.size(), "Incorrect list size: expected "
                + comments.size() + ", actual - " + commentDtoList.size());
        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(comment.getId()));
        assertThat(dto.getText(), equalTo(comment.getText()));
        assertThat(dto.getCreated(), equalTo(comment.getCreated()));
        assertThat(dto.getAuthorName(), equalTo(comment.getAuthor().getName()));
    }
}