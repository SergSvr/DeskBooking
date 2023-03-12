package com.education.booking.service.implementation;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.RoomDTO;
import com.education.booking.model.entity.Room;
import com.education.booking.model.enums.Status;
import com.education.booking.model.repository.RoomRepository;
import com.education.booking.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    public final RoomRepository roomRepository;
    private final ObjectMapper mapper;
    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        roomRepository.findByNumberAndStatus(roomDTO.getNumber(),Status.A).ifPresent(
                room -> {
                    throw new CustomException("Room already exists","create_room");
                }
        );
        Room room=new Room();
        room.setNumber(roomDTO.getNumber());
        room.setFloor(roomDTO.getFloor());
        room.setName(roomDTO.getName());
        room.setStatus(Status.A);
        Room save=roomRepository.save(room);
        return mapper.convertValue(save, RoomDTO.class);
    }
    @Override
    public void deleteRoom(Long id){
        Room room=getRoom(id);
        room.setStatus(Status.C);
        // Добавить удаление столов и букингов
        roomRepository.save(room);
    }
    @Override
    public Room getRoom(Long id) {
        return roomRepository.
                findByIdAndStatus(id, Status.A).
                orElseThrow(() -> new CustomException("Selected room not found","get_room"));
    }

    @Override
    public List<Room> getRooms() {
        return roomRepository.
                findAllByStatusOrderByFloorDesc(Status.A);
    }
    @Override
    public Room getRoomsByNumber(Long id) {
        return roomRepository.
                findByNumberAndStatus(id, Status.A).orElseThrow(() -> new CustomException("Selected room not found","get_room"));
    }


    @Override
    public RoomDTO update(Long id, RoomDTO roomDTO){
        Room room=getRoom(id);
        Room roomHist=new Room();
        roomHist.setStatus(Status.I);
        roomHist.setFloor(room.getFloor());
        roomHist.setNumber(room.getNumber());
        roomHist.setName(room.getName());
        roomRepository.save(roomHist);
        room.setName(roomDTO.getName());
        room.setFloor(roomDTO.getFloor());
        room.setNumber(roomDTO.getNumber());
        Room save=roomRepository.save(room);
        return mapper.convertValue(save, RoomDTO.class);
    }


}
