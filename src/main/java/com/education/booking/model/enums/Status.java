package com.education.booking.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    A("Active"),
    I("Inactive"),
    C("Deleted");

    private final String description;
}

