package com.github.rubenqba.inegi.domain;

import lombok.Value;

@Value
public class MxLocalty {
    String id;
    String state;
    String region;
    String name;
    LocaltyScope scope;
    Double latitude;
    Double longitude;
    Double altitude;
}
