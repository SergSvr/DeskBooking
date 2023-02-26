package com.education.booking.model.dto;

import com.education.booking.model.entity.Room;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeskDTO {
    String label;
    Long number;
    Room room;
}
