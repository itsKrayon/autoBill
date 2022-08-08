package com.ab.autoBill.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @author Keyan
 */
public class FileUtil {
    private static final String JPG = ".jpg";

    /**
     * 判断文件是不是图像文件
     * @param file
     * @return
     */
    public static boolean isImgFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf('.'));
        if(JPG.equals(suffixName)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 将文件存储到服务器src/main/file目录下
     * @param file 要存储的文件
     * @return 文件的路径
     */
    public static String saveFile(MultipartFile file, String parentDir, String subDir) {
        if (!file.isEmpty()) {
            //获取文件名
            String fileName = file.getOriginalFilename();
            try {
                assert fileName != null;
                //获取文件后缀
                String suffixName = fileName.substring(fileName.lastIndexOf('.'));
                //重新生成文件名
                fileName = UUID.randomUUID() + suffixName;
                File dir = new File(parentDir + "/main/file/"+ subDir);
                if (!dir.exists()) {
                    //创建文件存储根目录
                    dir.mkdirs();
                }
                String filePath = parentDir + "/main/file/"+ subDir+"/" + fileName;
                File f = new File(filePath);
                //将文件存储到本地
                file.transferTo(f);
                return filePath;
            } catch (Exception e) {
                return "error";
            }
        } else {
            return "error";
        }
    }
}
