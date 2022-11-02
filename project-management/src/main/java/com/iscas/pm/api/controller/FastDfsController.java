package com.iscas.pm.api.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.iscas.pm.api.util.FastDFSUtil;
import com.iscas.pm.common.core.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author by  lichang
 * @date 2022/8/19.
 *
 */

@RestController
@Api(tags = {"文件管理"})
@RequestMapping("/projectFile")
@ApiSort(7)
public class FastDfsController {
    @Autowired
    FastDFSUtil fastDFSUtil;
    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/uploadFile")
    @ApiOperation(value = "上传本地文件", notes = "上传本地文件到服务器")
    @ApiOperationSupport(order = 26)
    public String uploadTemplate(MultipartFile file) throws IOException {
        //文件存入FastDFs
        StorePath path = fastDFSUtil.upload(file);
        redisUtil.set(path.getFullPath(),null);

        //设置失效时间  (数值待定，单位：分   暂定是扫描时间间隔的两倍)
        redisUtil.expire(path.getFullPath(),10);
        return  path.getFullPath();
    }

    @GetMapping("/downloadFile")
    @ApiOperation(value = "下载文件", notes = "从服务器上下载文件,需要两个参数：路径为path，文件名重命名为请求参数name")
    @ApiOperationSupport(order = 27)
    public byte[] downloadTemplate(@RequestParam String path,@RequestParam String name,HttpServletResponse response) throws IOException {
           return      fastDFSUtil.download(path,name,response);
    }
}
