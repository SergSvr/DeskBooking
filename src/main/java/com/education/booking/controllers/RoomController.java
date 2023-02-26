package com.education.booking.controllers;

import com.education.booking.model.dto.RoomDTO;
import com.education.booking.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rooms")
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
//    @GetMapping
//    @Operation(summary = "получить сервисную запись")
//    public List<ServiceRecord> getRecords(@RequestParam String vin) {
//        return servRecService.getRecords(vin);
//    }
//
//
//    @DeleteMapping
//    @Operation(summary = "удалить сервисную запись")
//    public ResponseEntity<HttpStatus> deleteRecord(@RequestParam Long id) {
//        servRecService.delete(id);
//        return ResponseEntity.ok().build();
//    }

}
