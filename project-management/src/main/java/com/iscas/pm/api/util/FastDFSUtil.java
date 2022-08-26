package com.iscas.pm.api.util;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author： zhangchao
 * @Date： 2022/7/29
 * @Description：
 */
@Component
public class FastDFSUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 上传文件并返回文件路径
     *
     * @param file
     * @return 文件路径
     * @throws IOException
     */
    public StorePath upload(MultipartFile file) throws IOException {
        // 设置文件信息
        Set<MetaData> mataData = new HashSet<>();
        mataData.add(new MetaData("author", "iscas"));
        mataData.add(new MetaData("description", file.getOriginalFilename()));
        StorePath storePath = null;
        try {
            storePath = fastFileStorageClient.uploadFile(
                    file.getInputStream(),
                    file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()),
                    null);
        }catch (IOException e) {
            throw new IOException("上传文件异常："+e);
        }
        return storePath;
    }

    /**
     * 根据全路径删除
     *
     * @param path "fullPath": "group1/M00/00/00/wKgAkGIfF2KAG7qSAAB-jaFjG-Q678.jpg"
     */
    public void delete(String path) {
        fastFileStorageClient.deleteFile(path);
    }

    /**
     * 根据组和路径删除
     * "group": "group1",
     * "path": "M00/00/00/wKgAkGIfF2KAG7qSAAB-jaFjG-Q678.jpg",
     * "fullPath": "group1/M00/00/00/wKgAkGIfF2KAG7qSAAB-jaFjG-Q678.jpg"
     *
     * @param group
     * @param path
     */
    public void delete(String group, String path) {
        fastFileStorageClient.deleteFile(group, path);
    }

    /**
     * 文件下载
     *
     * @param path     文件路径，例如：/group1/M00/00/00/itstyle.png
     * @param filename 下载的文件命名
     * @return
     */
    public void download(String path, String filename, HttpServletResponse response) throws IOException {
        // 获取文件
        StorePath storePath = StorePath.parseFromUrl(path);
        if (StringUtils.isBlank(filename)) {
            filename = FilenameUtils.getName(storePath.getPath());
        }
        byte[] bytes = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        response.reset();

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, DEFAULT_CHARSET));
        response.setCharacterEncoding(DEFAULT_CHARSET);
        // 设置强制下载不打开
//        response.setContentType("application/force-download");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将传入的inputStream转换成MultipartFile并上传fastDFS
     *
     * @param inputStream inputStream
     * @param fileName    fileName
     * @return StorePath
     */
    public StorePath uploadByIO(InputStream inputStream, String fileName) throws IOException {
        MultipartFile file = getMultipartFile(inputStream, fileName);
        // 设置文件信息
        Set<MetaData> mataData = new HashSet<>();
        mataData.add(new MetaData("author", "iscas"));
        mataData.add(new MetaData("description", file.getOriginalFilename()));
        StorePath storePath = null;
        try {
            storePath = fastFileStorageClient.uploadFile(
                    file.getInputStream(),
                    file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()),
                    null);
        }catch (IOException e) {
            throw new IOException("上传文件异常："+e);
        }
        return storePath;
    }


    /**
     * 获取封装得MultipartFile
     *
     * @param inputStream inputStream
     * @param fileName    fileName
     * @return MultipartFile
     */
    public MultipartFile getMultipartFile(InputStream inputStream, String fileName) {
        FileItem fileItem = createFileItem(inputStream, fileName);
        //CommonsMultipartFile是feign对multipartFile的封装，但是要FileItem类对象
        return new CommonsMultipartFile(fileItem);
    }


    /**
     * FileItem类对象创建
     *
     * @param inputStream inputStream
     * @param fileName    fileName
     * @return FileItem
     */
    public FileItem createFileItem(InputStream inputStream, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[10 * 1024 * 1024];
        OutputStream os = null;
        //使用输出流输出输入流的字节
        try {
            os = item.getOutputStream();
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("文件上传失败");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new IllegalArgumentException("文件流关闭异常");
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new IllegalArgumentException("文件流关闭异常");
                }
            }
        }

        return item;
    }

}
