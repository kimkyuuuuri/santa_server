package com.smileflower.santa.src.flags.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class GetFlagRes {
    private int mountainIdx;
    private String mountain= new String();
    private int high;
}