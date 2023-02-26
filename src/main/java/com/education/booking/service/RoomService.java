package com.education.booking.service;

import com.education.booking.model.dto.RoomDTO;
import com.education.booking.model.entity.Room;

import java.util.List;

public interface RoomService {

    RoomDTO createRoom(RoomDTO roomDTO);

    void deleteRoom(Long id);

    Room getRoom(Long id);

    List<Room> getRooms();

    RoomDTO update(Long id, RoomDTO roomDTO);
}
