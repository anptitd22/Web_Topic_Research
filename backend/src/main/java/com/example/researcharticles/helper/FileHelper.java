package com.example.researcharticles.helper;

import org.springframework.web.multipart.MultipartFile;

public class FileHelper {
    public static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.endsWith("/jpeg") || contentType.endsWith("/png") || contentType.endsWith("/webp"));
    }

    public static boolean isValidDocument(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.endsWith(("/pdf")) ||
                contentType.endsWith("/doc") || // .doc
                contentType.endsWith("/docx") ||
                contentType.endsWith("/ppt") || // .ppt
                contentType.endsWith("/pptx") ||
                contentType.endsWith("/xls") || // .xls
                contentType.endsWith("/xlsx") ||
                contentType.endsWith("/txt") || // .txt
                contentType.endsWith("/msword") || // msword
                contentType.endsWith("/vnd.ms-excel") || // vnd.ms-excel
                contentType.endsWith("/opt")); // .opt
    }

    public static String createObjectKey(String prefix, String id, MultipartFile file) {
        String ext = getFileExtension(file);
        return String.format("%s/%s/%s.%s", prefix, id, java.util.UUID.randomUUID(), ext);
    }

    public static String getFileExtension(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return "";
        }
        return contentType.substring(contentType.lastIndexOf("/") + 1);
    }
}
