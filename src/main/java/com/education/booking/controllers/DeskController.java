package com.education.booking.controllers;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.DeskDTO;
import com.education.booking.model.entity.Room;
import com.education.booking.service.DeskService;
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
@RequestMapping("/desk")
@RequiredArgsConstructor
@Tag(name = "Столы")
public class DeskController {
    private final DeskService deskService;
    private final RoomService roomService;

    @ExceptionHandler(CustomException.class)
    public ModelAndView handler(CustomException exception){
        ModelMap model=new ModelMap();
        model.put("error", exception.getMessage());
        return getDesks(model);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            ConversionFailedException.class,
            IllegalArgumentException.class
    })
    public ModelAndView handlerOtherExceptions() {
        ModelMap model = new ModelMap();
        model.put("error", "Введены некорректные данные");
        return getDesks(model);
    }

    @PostMapping
    @Operation(summary = "создать стол")
    public ModelAndView createDesk(@ModelAttribute DeskDTO deskDTO, ModelMap model) {
        model.put("result",deskService.createDesk(deskDTO));
        return getDesks(model);
    }

    @GetMapping("/delete")
    @Operation(summary = "удалить стол")
    public ModelAndView deleteDesk(@RequestParam Long id, ModelMap model) {
        deskService.deleteDesk(id);
        return getDesks(model);
    }

    @GetMapping
    @Operation(summary="получить список столов")
    public ModelAndView getDesks(ModelMap model)
    {   List<DeskDTO> desks=deskService.getDesks();
        model.put("desks",desks);
        List<Room> rooms = roomService.getRooms();
        model.put("rooms", rooms);
        return new ModelAndView("desk",model);
    }
}
