package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemInfoDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", updatable = false, insertable = false)
    private List<CommentDto> comments;
}
