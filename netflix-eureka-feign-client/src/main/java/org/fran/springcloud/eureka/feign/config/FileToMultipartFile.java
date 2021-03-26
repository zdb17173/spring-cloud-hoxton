package org.fran.springcloud.eureka.feign.config;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author fran
 * @Description
 * @Date 2020/1/7 10:38
 */
public class FileToMultipartFile {

    public static MultipartFile getMulFile(File file) {
        if(file!= null && file.exists()){
            FileItem fileItem = createFileItem(file);
            MultipartFile mfile = new CommonsMultipartFile(fileItem);
            return mfile;
        }else
            throw new IllegalArgumentException();


    }

    public static FileItem createFileItem(File file)
    {
        String filePath = file.getPath();
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "uploadFile";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "multipart/form-data", true,
                file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try
        {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return item;
    }
}
