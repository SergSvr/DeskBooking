package com.education.booking.controllers;

import com.education.booking.model.dto.DeskDTO;
import com.education.booking.service.DeskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/desks")
@RequiredArgsConstructor
@Tag(name = "Столы")
public class DeskController {
    private final DeskService deskService;

    @PostMapping
    @Operation(summary = "создать стол")
    public ModelAndView createRoom(@ModelAttribute DeskDTO deskDTO) {
        deskService.createDesk(deskDTO);
        return new ModelAndView("desklist");
    }

    @GetMapping
    @Operation(summary="получить список столов")
    public ModelAndView getRooms()
    {
        return new ModelAndView("desklist");
    }
}
