package com.education.booking.service.implementation;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.DeskDTO;
import com.education.booking.model.entity.Desk;
import com.education.booking.model.entity.Room;
import com.education.booking.model.enums.Status;
import com.education.booking.model.repository.DeskRepository;
import com.education.booking.model.repository.RoomRepository;
import com.education.booking.service.DeskService;
import com.education.booking.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeskServiceImpl implements DeskService {

    public final DeskRepository deskRepository;
    public final RoomRepository roomRepository;
    public final RoomService roomService;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public DeskDTO createDesk(DeskDTO deskDTO) {
        if (deskDTO.getRoom()==null)
            throw new CustomException("Не указана комната для стола", HttpStatus.BAD_REQUEST);
        deskRepository.findByNumberAndStatusAndRoom(deskDTO.getNumber(), Status.A, deskDTO.getRoom()).ifPresent(
                driver -> {
                    throw new CustomException("Стол уже существует", HttpStatus.BAD_REQUEST);
                }
        );
        Desk desk = new Desk();
        desk.setNumber(deskDTO.getNumber());
        desk.setLabel(deskDTO.getLabel());
        desk.setStatus(Status.A);
        Room room=roomService.getRoom(deskDTO.getRoom().getId());
        desk.setRoom(room);
        room.getDesks().add(desk);
        roomRepository.save(room);
        Desk save = deskRepository.save(desk);
        return mapper.convertValue(save, DeskDTO.class);
    }

    @Override
    public void deleteDesk(Long id) {
        Desk desk = getDesk(id);
        desk.setStatus(Status.C);
        // Добавить букингов
        deskRepository.save(desk);
    }

    @Override
    public Desk getDesk(Long id) {
        return deskRepository.
                findByIdAndStatus(id, Status.A).
                orElseThrow(() -> new CustomException("Стол с таким id не найден", HttpStatus.NOT_FOUND));
    }

    @Override
    public DeskDTO updateDesk(Long id, DeskDTO deskDTO) {
        Desk desk = getDesk(id);
        desk.setNumber(deskDTO.getNumber());
        desk.setLabel(deskDTO.getLabel());
        Desk save = deskRepository.save(desk);
        return mapper.convertValue(save, DeskDTO.class);
    }

    @Override
    public DeskDTO changeRoom(Long deskId, Long roomId) {
        Desk desk = getDesk(deskId);
        Room room = roomService.getRoom(roomId);
        room.getDesks().add(desk);
        roomRepository.save(room);
        Desk save = deskRepository.save(desk);
        return mapper.convertValue(save, DeskDTO.class);
    }
}
