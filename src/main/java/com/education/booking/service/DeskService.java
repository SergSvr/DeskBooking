package com.education.booking.service;

import com.education.booking.model.dto.DeskDTO;
import com.education.booking.model.entity.Desk;

public interface DeskService {

    DeskDTO createDesk(DeskDTO deskDTO);

    void deleteDesk(Long id);

    Desk getDesk(Long id);

    DeskDTO updateDesk(Long id, DeskDTO deskDTO);

    DeskDTO changeRoom(Long deskId, Long roomId);
}
