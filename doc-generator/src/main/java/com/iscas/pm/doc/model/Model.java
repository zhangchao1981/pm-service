package com.iscas.pm.doc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Model {

    private String name;
    private List<Case> cases;
    private List<Model> subModels;
}
