package com.udacity.jwdnd.course1.cloudstorage.Services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFilenameAvailable(String fileName, Integer userId) {
        return (this.fileMapper.getFile(fileName, userId) != null);
    }

    public void createFile(MultipartFile multipartFile, Integer userId) throws IOException {

        File file= new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setContentType(multipartFile.getContentType());
        file.setFileData(multipartFile.getBytes());
        file.setFileSize(multipartFile.getSize());
        file.setUserId(userId);

        fileMapper.insert(file);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }

    public Object getAllFilesByUserId(Integer userId) {
        return fileMapper.getAllFilesByUserId(userId);
    }
}
