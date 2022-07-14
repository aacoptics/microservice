package com.aacoptics.sale.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SalesUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tabUrl;

   private String userid;

    private String userNo;

}