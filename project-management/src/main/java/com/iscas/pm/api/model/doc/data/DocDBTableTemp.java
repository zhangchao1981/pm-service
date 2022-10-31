package com.iscas.pm.api.model.doc.data;

import com.iscas.pm.api.model.doc.TableFieldInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/9/30.
 */

@Data
@Accessors(chain = true)
public class DocDBTableTemp {

    private  String dBTableName;

    private String dBTableComment;

    private List<TableFieldInfo> dBTableStructList;

}
