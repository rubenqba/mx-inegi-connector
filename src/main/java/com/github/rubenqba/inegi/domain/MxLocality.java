package com.github.rubenqba.inegi.domain;

import lombok.Value;

@Value
public class MxLocality {
    String id;
    String state;
    String municipal;
    String name;
    LocaltyScope scope;
    Double latitude;
    Double longitude;
}
