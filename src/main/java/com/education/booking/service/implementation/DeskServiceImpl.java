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
import java.util.List;
import java.util.stream.Collectors;

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
        if (deskDTO.getRoomNumber() == null)
            throw new CustomException("Room number was not set","create_desk", HttpStatus.BAD_REQUEST);
        Room room = roomService.getRoomsByNumber(deskDTO.getRoomNumber());
        deskRepository.findByNumberAndStatusAndRoom(deskDTO.getNumber(), Status.A, room).ifPresent(
                desk -> {
                    throw new CustomException("Desk already exists","create_desk", HttpStatus.BAD_REQUEST);
                }
        );
        Desk desk = new Desk();
        desk.setNumber(deskDTO.getNumber());
        desk.setLabel(deskDTO.getLabel());
        desk.setStatus(Status.A);
        desk.setRoom(room);
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
                orElseThrow(() -> new CustomException("Desk with id is not found","get_desk", HttpStatus.NOT_FOUND));
    }

    @Override
    public Desk getDeskByNumber(Long id) {
        return deskRepository.
                findByNumberAndStatus(id, Status.A);
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

    @Override
    @Transactional
    public List<DeskDTO> getDesks() {
        return deskRepository.findAllByStatusOrderByRoomDesc(Status.A)
                .stream()
                .map(h ->
                {
                    DeskDTO deskDTO = new DeskDTO();
                    deskDTO.setRoomNumber(h
                            .getRoom()
                            .getNumber()
                    );
                    deskDTO.setLabel(h.getLabel());
                    deskDTO.setId(h.getId());
                    deskDTO.setNumber(h.getNumber());
                    return deskDTO;
                })
                .collect(Collectors.toList());
    }
}
