package com.doc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.domain.Pjdata;
import com.doc.service.PjdataService;
import com.doc.mapper.PjdataMapper;
import com.doc.util.DocumentHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
* @author 66410
* @description 针对表【pjdata】的数据库操作Service实现
* @createDate 2022-07-04 13:44:25
*/
@Service
public class PjdataServiceImpl extends ServiceImpl<PjdataMapper, Pjdata>
    implements PjdataService{
    @Resource
    private PjdataMapper pjdataMapper;


    @Override
    public boolean createword(int id, String filename) {
        Pjdata pjdata = pjdataMapper.selectById(id);
        if (pjdata!=null){
            DocumentHandler documentHandler = new DocumentHandler();
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("pjname",pjdata.getPjname());
            dataMap.put("pjnumber",pjdata.getPjnumber());
            dataMap.put("pjstage",pjdata.getPjstage());
            //这里只用于word文档内容填充
            //待完成功能2： word文档的表格内容填充--->要把表格数据封装为一个表格对象里，直接放到dataMap里
            //待完成功能3： word文档的表单内容填充-->把数据封装到list<对象> 里，然后放到dataMap中
            //注意1：填充内容中有换行，需将"\n"全部替换成"<w:p></w:p>"，
            //注意2：如果填充的数据字符串中含有特殊字符< 、>、&  则会出现问题

            try {
                DocumentHandler.createDoc(dataMap,"D:/"+filename+".doc");//输出到D:/文件名.doc
            } catch (UnsupportedEncodingException e) {
                return  false;
            }
        }else {return  false;}
        return true;
    }
}




