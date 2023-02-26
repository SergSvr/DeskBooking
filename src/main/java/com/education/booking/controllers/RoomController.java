package com.education.booking.controllers;

import com.education.booking.model.dto.RoomDTO;
import com.education.booking.model.entity.Room;
import com.education.booking.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Tag(name = "Комнаты")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "создать комнату")
    public RoomDTO createRoom(@ModelAttribute RoomDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

//    @PutMapping
//    @Operation(summary = "обновить сервисную запись")
//    public ResponseEntity<ServiceRecord> updateRecord(@RequestBody ServiceRecord serviceRecord) {
//        return ResponseEntity.ok(servRecService.update(serviceRecord));
//    }
//
//
    @GetMapping
    @Operation(summary = "получить список комнат")
    public ModelAndView getRooms(ModelMap model) {
        List<Room> rooms=roomService.getRooms();
        model.put("rooms",rooms);
        return new ModelAndView("room", model);
    }
//
//@GetMapping
//@Operation(summary = "получить список комнат")
//public ModelAndView getRooms(ModelAndView model) {
//    List<Room> rooms=roomService.getRooms();
//    model.setViewName("room");
//    model.addObject("rooms",rooms);
//    return model;
//}
//
//    @DeleteMapping
//    @Operation(summary = "удалить сервисную запись")
//    public ResponseEntity<HttpStatus> deleteRecord(@RequestParam Long id) {
//        servRecService.delete(id);
//        return ResponseEntity.ok().build();
//    }

}
