package com.doc.controller;

import com.doc.domain.Pjdata;
import com.doc.mapper.PjdataMapper;
import com.doc.service.PjdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 李昶
 * @since 2022-07-03
 */
@RestController
@RequestMapping("/pjdata")  //
public class PjdataContoller {
    @Resource
    private PjdataMapper pjdataMapper;
    @Autowired
    private PjdataService pjdataService;

    //改成Project    findprojectbyId
    @PostMapping(value = "/search/{id}")
    public Pjdata findbyId(@PathVariable Integer id){
        return pjdataMapper.selectById(id);               //弄到service层
    }

    //    @PostMapping(value = "/create")
//    public void create(@RequestBody(required = false) ContentCategory contentCategory, @PathVariable int page, @PathVariable int size) {
//        //调用ContentCategoryService实现分页条件查询ContentCategory
//        PageInfo<ContentCategory> pageInfo = contentCategoryService.findPage(contentCategory, page, size);
//        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
//    }
    //给data的 id  返回对应的dataMap
    @RequestMapping(value = "/getdata/{id}")
    public Pjdata findbyid(@PathVariable Integer id) {
        return pjdataMapper.selectById(id);
    }

  //加一个controller
    //    //给dataMap对应id ，  自动生成对应的文档
    @PostMapping(value = "/create/{id}")
    public boolean createword(@PathVariable int id) throws UnsupportedEncodingException {
        //首先调用service查找data的方法，然后命名文件名
        String filename="test"+id;
        //生成对应的文件，返回结果是否成功
        return    pjdataService.createword(id,filename);
    }
}
