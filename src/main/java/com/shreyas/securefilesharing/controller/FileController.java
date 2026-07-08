package com.shreyas.securefilesharing.controller;

import com.shreyas.securefilesharing.entity.FileEntity;
import com.shreyas.securefilesharing.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public FileEntity upload(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        return fileService.uploadFile(file);

    }

    @GetMapping("/my")
    public List<FileEntity> myFiles() {

        return fileService.getMyFiles();

    }
}