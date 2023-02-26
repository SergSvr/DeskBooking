package com.education.booking.service.implementation;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.BookingDTO;
import com.education.booking.model.entity.Booking;
import com.education.booking.model.entity.Desk;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;

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
    public BookingDTO createBooking(BookingDTO bookingDTO, String mail, Long id) {
        Desk desk=deskService.getDesk(id);
        User user=userService.getUser(mail);
        if (bookingDTO.getBookingDate()==null || bookingDTO.getStartTime()==null || bookingDTO.getEndTime()==null)
            throw new CustomException("Не хватает данных для бронирования", HttpStatus.BAD_REQUEST);
        bookingRepository.findAllByBookingDateAndStatusAndDesk(bookingDTO.getBookingDate(),Status.A, desk).forEach(
                booking -> {
                  if(Duration.between(booking.getStartTime(),bookingDTO.getEndTime()).toMinutes()<0
                            || Duration.between(booking.getEndTime(),bookingDTO.getStartTime()).toMinutes()<0)
                       throw new CustomException("Бронирование уже существует", HttpStatus.BAD_REQUEST);
                }
        );
        Booking booking=new Booking();
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        booking.setDesk(desk);
        booking.setUser(user);
        booking.setStatus(Status.A);
        Booking save=bookingRepository.save(booking);
        return mapper.convertValue(save,BookingDTO.class);
    }

    @Override
    public void deleteBooking(Long id){
        Booking booking=getBooking(id);
        booking.setStatus(Status.C);
        bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long id) {
        return bookingRepository.
                findByIdAndStatus(id, Status.A).
                orElseThrow(() -> new CustomException("Бронирование с таким id не найдено", HttpStatus.NOT_FOUND));
    }
}
