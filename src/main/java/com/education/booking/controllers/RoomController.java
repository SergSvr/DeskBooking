package com.education.booking.controllers;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.RoomDTO;
import com.education.booking.model.entity.Room;
import com.education.booking.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Tag(name = "Комнаты")
public class RoomController {
    private final RoomService roomService;

    @ExceptionHandler(CustomException.class)
    public ModelAndView handler(CustomException exception,Authentication authentication){
        ModelMap model=new ModelMap();
        model.put("error", exception.getMessage());
        return getRooms(authentication,model);
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            ConversionFailedException.class,
            IllegalArgumentException.class
    })
    public ModelAndView handlerOtherExceptions(Authentication authentication) {
        ModelMap model = new ModelMap();
        model.put("error", "Введены некорректные данные");
        return getRooms(authentication,model);
    }

    @PostMapping
    @Operation(summary = "создать комнату")
    public ModelAndView createRoom(@ModelAttribute RoomDTO roomDTO, Authentication authentication, ModelMap model) {
        model.put("result", roomService.createRoom(roomDTO));
        return getRooms(authentication,model);
    }

    @GetMapping
    @Operation(summary = "получить список комнат")
    public ModelAndView getRooms(Authentication authentication, ModelMap model) {
        List<Room> rooms = roomService.getRooms();
        model.put("rooms", rooms);
        return new ModelAndView("room", model);
    }

    @GetMapping("/delete")
    @Operation(summary = "удалить комнату")
    public ModelAndView deleteRoom(@RequestParam Long id, Authentication authentication, ModelMap model) {
        roomService.deleteRoom(id);
        return getRooms(authentication,model);
    }


}
