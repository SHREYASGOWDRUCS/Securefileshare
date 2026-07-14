package com.shreyas.securefilesharing.controller;

import com.shreyas.securefilesharing.entity.FileEntity;
import com.shreyas.securefilesharing.service.FileService;
import com.shreyas.securefilesharing.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public FileResponse upload(@RequestParam("file") MultipartFile file) throws Exception {

        return fileService.uploadFile(file);
    }

    @GetMapping("/my")
    public List<FileResponse> myFiles() {

        return fileService.getMyFiles();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<String> download(@PathVariable Long id) {

        return ResponseEntity.ok(fileService.downloadFile(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {

        fileService.deleteFile(id);

        return ResponseEntity.ok("File deleted successfully");
    }
}