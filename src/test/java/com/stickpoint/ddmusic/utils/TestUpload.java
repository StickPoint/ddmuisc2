package com.stickpoint.ddmusic.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author fntp
 * @date 2025/9/25
 */
public class TestUpload {

    public static void main(String[] args) throws IOException {
    ////    先获取文件地址 "C:\Users\fntp\Downloads\document_6156969565961065492.mp4"
        String filePath = "C:\\Users\\fntp\\Downloads\\document_6156969565961065492.mp4";
        File file = new File(filePath);
        String cookie = "_ga=GA1.1.1204436089.1758720937; PHPSESSID=915h0l07bk883vml3p7d62l9it; _ga_DBMQHG266Y=GS2.1.s1758804011$o2$g1$t1758804041$j30$l0$h0";
        String resp = HttpUtils.uploadToUhsea("https://www.uhsea.com/Frontend/upload", file, cookie);
        System.out.println("上传结果: " + resp);
    //    long fileSiz = new File(filePath).length();
    //    String fileMd5 = FileUtil.getFileMd5(filePath);
    //    long lastModified = new File(filePath).lastModified();
    //    String uploadResult = HttpUtils.uploadBigFile(filePath, fileSiz, lastModified, fileMd5);
    //    System.out.println(uploadResult);
    }

}
