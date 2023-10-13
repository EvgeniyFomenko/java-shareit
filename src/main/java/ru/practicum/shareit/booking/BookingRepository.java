package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByItemIdOwnerAndStatusOrderByIdDesc(long idOwner, StatusBooking status);

    Booking findByItemId(long id);

    List<Booking> findAllByItemIdOwnerOrderByIdDesc(long userId);

    List<Booking> findAllByIdBookerOrderByIdDesc(long userId);

    List<Booking> findAllByIdBookerAndStatus(long userId, StatusBooking statusBooking);

    List<Booking> findAllByItemIdOwnerAndStatus(long userId, StatusBooking statusBooking);

    List<Booking> findAllByIdBookerAndStartIsAfterOrderByStartDesc(long bookerId, LocalDateTime start);

    List<Booking> findAllByItemIdOwnerAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime now);

    Booking findFirstByItemIdOwnerAndStartIsBeforeAndStatusOrderByStartDesc(long userId, LocalDateTime now, StatusBooking statusBooking);

    Booking findFirstByItemIdOwnerAndStartIsAfterAndStatusOrderByStartAsc(long userId, LocalDateTime now, StatusBooking statusBooking);

    Optional<Booking> findFirstByIdBookerAndItemIdAndStatusOrderByEndAsc(long authorId, long itemId, StatusBooking approved);

    List<Booking> findAllByIdBookerAndStartIsBeforeAndEndIsAfter(long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByIdBookerAndStatusAndStartIsBeforeAndEndIsBeforeOrderByIdDesc(long userId, StatusBooking statusBooking, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByItemIdOwnerAndStartIsBeforeAndEndIsAfter(long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByItemIdOwnerAndStatusAndStartIsBeforeAndEndIsBeforeOrderByIdDesc(long userId, StatusBooking statusBooking, LocalDateTime now, LocalDateTime now1);
}
