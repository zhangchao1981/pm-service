package com.iscas.pm.api.model.doc.data;

import com.iscas.pm.api.model.doc.TableByDB;
import com.iscas.pm.api.model.doc.TableStructure;
import lombok.Builder;
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

    private  String tableName;

    private List<TableStructure>  tableStructureList;

}
