package com.education.booking.service;

import com.education.booking.model.dto.RoomDTO;
import com.education.booking.model.entity.Room;

public interface RoomService {

    RoomDTO createRoom(RoomDTO roomDTO);

    void deleteRoom(Long id);

    Room getRoom(Long id);

    RoomDTO update(Long id, RoomDTO roomDTO);
}
