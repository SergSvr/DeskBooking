package com.education.booking.service.implementation;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.BookingDTO;
import com.education.booking.model.dto.DeskDTO;
import com.education.booking.model.entity.Booking;
import com.education.booking.model.entity.Desk;
import com.education.booking.model.entity.Room;
import com.education.booking.model.entity.User;
import com.education.booking.model.enums.Status;
import com.education.booking.model.repository.BookingRepository;
import com.education.booking.model.repository.DeskRepository;
import com.education.booking.model.repository.UserRepository;
import com.education.booking.service.BookingService;
import com.education.booking.service.DeskService;
import com.education.booking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    public final BookingRepository bookingRepository;
    public final DeskService deskService;
    public final UserService userService;
    public final DeskRepository deskRepository;
    public final UserRepository userRepository;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO, String mail, Long number) {
        Desk desk = deskService.getDeskByNumber(number);
        User user = userService.getUser(mail);
        if (desk == null || user == null || bookingDTO.getBookingDate() == null || bookingDTO.getStartTime() == null || bookingDTO.getEndTime() == null)
            throw new CustomException("Не хватает данных для бронирования", "create_booking");
        if (bookingDTO.getEndTime().isBefore(bookingDTO.getStartTime()) || bookingDTO.getEndTime().compareTo(bookingDTO.getStartTime()) == 0) {
            throw new CustomException("Некорректный временной интервал", "create_booking");
        }
        bookingRepository.findAllByBookingDateAndStatusAndDesk(bookingDTO.getBookingDate(), Status.A, desk).forEach(
                booking -> {
                    if (booking.getStartTime().isBefore(bookingDTO.getEndTime())
                            &&
                            booking.getEndTime().isAfter(bookingDTO.getStartTime()))
                        throw new CustomException("Бронирование уже существует", "create_booking");
                }
        );
        Booking booking = new Booking();
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        booking.setDesk(desk);
        booking.setUser(user);
        booking.setStatus(Status.A);
        Booking save = bookingRepository.save(booking);
        return mapper.convertValue(save, BookingDTO.class);
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = getBooking(id);
        booking.setStatus(Status.C);
        bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long id) {
        return bookingRepository.
                findByIdAndStatus(id, Status.A).
                orElseThrow(() -> new CustomException("Бронирование с таким id не найдено", "get_booking"));
    }

    @Override
    @Transactional
    public List<Booking> getBookings() {
        return bookingRepository.findAllByBookingDateGreaterThanEqualAndStatus(LocalDate.now(), Status.A);
    }

    @Override
    @Transactional
    public List<Booking> getBookingsByUser(String mail) {
        User user = userService.getUser(mail);
        return bookingRepository.findAllByUserAndStatusAndBookingDateGreaterThanEqual(user, Status.A, LocalDate.now());
    }

    @Override
    @Transactional
    public List<DeskDTO> getAvailableDesks(BookingDTO bookingDTO){
        List<Desk> desks=deskRepository.findAllByStatusOrderByRoomDesc(Status.A);
        List<DeskDTO> result=new ArrayList<>();
        Iterator<Desk> i = desks.iterator();
        while (i.hasNext()) {
            Desk temp = i.next();
            bookingRepository.findAllByBookingDateAndStatusAndDesk(bookingDTO.getBookingDate(), Status.A, temp).forEach(
                    booking -> {
                        if (booking.getStartTime().isBefore(bookingDTO.getEndTime())
                                &&
                                booking.getEndTime().isAfter(bookingDTO.getStartTime()))
                            i.remove();
                    }
            );
        }
        desks.forEach(
                (desk) -> {
            DeskDTO deskDTO=mapper.convertValue(desk, DeskDTO.class);
            deskDTO.setRoomNumber(desk.getRoom().getNumber());
            result.add(deskDTO);
        });
        return result;
    }
}
