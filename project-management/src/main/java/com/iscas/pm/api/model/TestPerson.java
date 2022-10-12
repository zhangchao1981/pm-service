package com.iscas.pm.api.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.poifs.property.Child;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/10/11.
 */
@Data
@Accessors(chain = true)
public class TestPerson {
    private Family  family;
}
