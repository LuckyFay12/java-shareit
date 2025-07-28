package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItemAndBooker(Item item, User booker);

    //вся история бронирований
    List<Booking> findAllByItemOwnerOrderByStartDesc(@Param("owner") User owner);

    //поиск текущих бронирований определенного пользователя
    @Query("select b from Booking b " +
           "where b.item.owner = :owner and :now between b.start and b.end " +
           "order by b.start desc")
    List<Booking> findCurrentByOwnerItems(@Param("owner") User owner, @Param("now") LocalDateTime now);

    @Query("select b from Booking b " +
           "where b.item.owner = :owner and b.end < :now " +
           "order by b.start desc")
    List<Booking> findPastByOwnerItems(@Param("owner") User owner, @Param("now") LocalDateTime now);

    @Query("select b from Booking b " +
           "where b.item.owner = :owner and b.start > :now " +
           "order by b.start desc")
    List<Booking> findFutureByOwnerItems(@Param("owner") User owner, @Param("now") LocalDateTime now);

    @Query("select b from Booking b " +
           "where b.item.owner = :owner and b.status in :statuses " +
           "order by b.start desc")
    List<Booking> findAllByOwnerItemsAndStatusList(@Param("owner") User owner, @Param("statuses") List<Status> statuses);

    @Query("select b from Booking b " +
           "where b.booker = :booker and b.status in :statuses " +
           "order by b.start desc")
    List<Booking> findAllByBookerAndStatusList(@Param("booker") User booker, @Param("statuses") List<Status> statuses);

    @Query("select b from Booking b " +
           "where b.booker = :booker and :now between b.start and b.end " +
           "order by b.start desc")
    List<Booking> findCurrentByBookerItems(@Param("booker") User booker, @Param("now") LocalDateTime now);

    @Query("select b from Booking b " +
           "where b.booker = :booker and b.end < :now " +
           "order by b.start desc")
    List<Booking> findPastByBookerItems(@Param("booker") User booker, @Param("now") LocalDateTime now);

    @Query("select b from Booking b " +
           "where b.booker = :booker and b.start > :now " +
           "order by b.start desc")
    List<Booking> findFutureByBookerItems(@Param("booker") User booker,
                                          @Param("now") LocalDateTime now);

    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    Optional<Booking> findFirstByItemAndStartAfterAndStatusInOrderByStartAsc(Item item, LocalDateTime now, List<Status> statuses);

    Optional<Booking> findFirstByItemAndEndBeforeAndStatusInOrderByEndDesc(Item item, LocalDateTime now, List<Status> statuses);
}
