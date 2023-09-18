package ru.practicum.shareit.booking.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.type.StatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    @NotNull
    Optional<Booking> findById(@NotNull Long bookingId);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Booking as b where b.booker.id = ?1 order by b.startTime desc")
    List<Booking> findAllByBookerIdAndOrderByStartTimeDesc(Long bookerId);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfterOrderByStartTimeAsc(
            Long bookerId, LocalDateTime nowStart, LocalDateTime nowEnd);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsBeforeOrderByStartTimeDesc(
            Long bookerId, LocalDateTime nowStart, LocalDateTime nowEnd);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByBookerIdAndStartTimeIsAfterAndEndTimeIsAfterOrderByStartTimeDesc(
            Long bookerId, LocalDateTime nowStart, LocalDateTime nowEnd);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByBookerIdAndStatusOrderByStartTimeDesc(Long bookerId, StatusType statusType);

    @EntityGraph(value = "Booking.entity")
    @Query("select b from Booking as b where b.item.owner.id = ?1 order by b.startTime desc")
    List<Booking> findAllByItemOwnerIdAndOrderByStartTimeDesc(Long ownerId);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfterOrderByStartTimeDesc(
            Long ownerId, LocalDateTime nowStart, LocalDateTime nowEnd);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsBeforeOrderByStartTimeDesc(
            Long ownerId, LocalDateTime nowStart, LocalDateTime nowEnd);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByItemOwnerIdAndStartTimeIsAfterAndEndTimeIsAfterOrderByStartTimeDesc(
            Long ownerId, LocalDateTime nowStart, LocalDateTime nowEnd);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartTimeDesc(Long ownerId, StatusType statusType);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Booking as b " +
            "where b.item.id = ?1 and b.item.owner.id = ?2 and b.startTime <= ?3 " +
            "order by b.endTime desc")
    List<Booking> findLastBookingByItemId(Long itemId, Long userId, LocalDateTime now);

    @EntityGraph(value = "Booking.entity", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Booking as b " +
            "where b.item.id = ?1 and b.item.owner.id = ?2 and b.startTime >= ?3 " +
            "order by b.startTime asc")
    List<Booking> findNextBookingByItemId(Long itemId, Long userId, LocalDateTime now);

    List<Booking> findBookingsByItemIdAndBookerId(Long itemId, Long bookerId);
}
