package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByItemIdOwnerAndStatusOrderByIdDesc(long idOwner, StatusBooking status);

    Booking findByItemId(long id);

    Page<Booking> findAllByItemIdOwnerOrderByIdDesc(long userId, Pageable id);

    Page<Booking> findAllByIdBookerOrderByIdDesc(long userId, Pageable id);

    Page<Booking> findAllByIdBookerAndStatus(long userId, StatusBooking statusBooking, Pageable id);

    Page<Booking> findAllByItemIdOwnerAndStatus(long userId, StatusBooking statusBooking, Pageable id);

    Page<Booking> findAllByIdBookerAndStartIsAfterOrderByStartDesc(long bookerId, LocalDateTime start, Pageable id);

    Page<Booking> findAllByItemIdOwnerAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime now, Pageable id);

    Booking findFirstByItemIdOwnerAndStartIsBeforeAndStatusOrderByStartDesc(long userId, LocalDateTime now, StatusBooking statusBooking);

    Booking findFirstByItemIdOwnerAndStartIsAfterAndStatusOrderByStartAsc(long userId, LocalDateTime now, StatusBooking statusBooking);

    Optional<Booking> findFirstByIdBookerAndItemIdAndStatusOrderByEndAsc(long authorId, long itemId, StatusBooking approved);

    Page<Booking> findAllByIdBookerAndStartIsBeforeAndEndIsAfter(long userId, LocalDateTime now, LocalDateTime now1, Pageable id);

    Page<Booking> findAllByIdBookerAndStatusAndStartIsBeforeAndEndIsBeforeOrderByIdDesc(long userId, StatusBooking statusBooking, LocalDateTime now, LocalDateTime now1, Pageable id);

    Page<Booking> findAllByItemIdOwnerAndStartIsBeforeAndEndIsAfter(long userId, LocalDateTime now, LocalDateTime now1, Pageable id);

    Page<Booking> findAllByItemIdOwnerAndStatusAndStartIsBeforeAndEndIsBeforeOrderByIdDesc(long userId, StatusBooking statusBooking, LocalDateTime now, LocalDateTime now1, Pageable id);
}
