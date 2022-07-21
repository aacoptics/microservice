package com.aacoptics.coating.dashboard.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CoatingStatusEntities implements Serializable {
    private List<CoatingStatusEntity> Site;
}
