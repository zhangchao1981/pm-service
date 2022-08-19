package com.iscas.pm.api.model.doc.param;

import com.iscas.pm.api.model.doc.ReferenceDoc;
import com.iscas.pm.api.model.doc.ReviseRecord;
import lombok.Data;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/19.
 */
@Data
public class CreateDocumentParam {

    private String templatePath;

    private  List<ReferenceDoc>  referenceDocList;

    private List<ReviseRecord>  reviseRecordList;


}
