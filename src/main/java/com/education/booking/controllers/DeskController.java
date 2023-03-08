package com.education.booking.controllers;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.DeskDTO;
import com.education.booking.model.entity.Room;
import com.education.booking.service.DeskService;
import com.education.booking.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.education.booking.controllers.UserController.getUser;

@RestController
@RequestMapping("/desk")
@RequiredArgsConstructor
@Tag(name = "Столы")
public class DeskController {
    private final DeskService deskService;
    private final RoomService roomService;

    @ExceptionHandler(CustomException.class)
    public ModelAndView handler(CustomException exception,Authentication authentication){
        ModelMap model=new ModelMap();
        model.put("error", exception.getMessage());
        return getDesks(authentication, model);
    }

    @PostMapping
    @Operation(summary = "создать стол")
    public ModelAndView createDesk(@ModelAttribute DeskDTO deskDTO,Authentication authentication, ModelMap model) {
        model.put("result",deskService.createDesk(deskDTO));
        return getDesks(authentication, model);
    }

    @GetMapping("/delete")
    @Operation(summary = "удалить стол")
    public ModelAndView deleteDesk(@RequestParam Long id,Authentication authentication, ModelMap model) {
        deskService.deleteDesk(id);
        return getDesks(authentication, model);
    }

    @GetMapping
    @Operation(summary="получить список столов")
    public ModelAndView getDesks(Authentication authentication, ModelMap model)
    {   List<DeskDTO> desks=deskService.getDesks();
        model.put("desks",desks);
        List<Room> rooms = roomService.getRooms();
        model.put("rooms", rooms);
        getUser(authentication, model);
        return new ModelAndView("desk",model);
    }
}
