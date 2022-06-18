package com.aacoptics.sep.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SepTokenResult implements Serializable {

    public String username;

    public String token;
}
