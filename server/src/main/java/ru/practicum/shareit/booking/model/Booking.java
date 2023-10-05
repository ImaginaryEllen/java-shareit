package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(
        name = "Booking.entity",
        attributeNodes = {
                @NamedAttributeNode(value = "item", subgraph = "item-owner"),
                @NamedAttributeNode(value = "booker")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "item-owner", attributeNodes = {
                        @NamedAttributeNode("owner")
                }
                )
        }
)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false)
    private Long id;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    private StatusType status;
}
