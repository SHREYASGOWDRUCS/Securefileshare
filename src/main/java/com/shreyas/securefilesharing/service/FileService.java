package com.shreyas.securefilesharing.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shreyas.securefilesharing.entity.FileEntity;
import com.shreyas.securefilesharing.entity.User;
import com.shreyas.securefilesharing.repository.FileRepository;
import com.shreyas.securefilesharing.repository.UserRepository;
import com.shreyas.securefilesharing.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    public FileResponse uploadFile(MultipartFile file) throws Exception {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        String fileUrl = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();

        FileEntity entity = FileEntity.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .fileUrl(fileUrl)
                .publicId(publicId)
                .uploadedAt(LocalDateTime.now())
                .user(user)
                .build();

        FileEntity savedFile = fileRepository.save(entity);
        return mapToResponse(savedFile);    }

    public List<FileResponse> getMyFiles() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return fileRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public String downloadFile(Long id) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FileEntity file = fileRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("File not found"));

        return file.getFileUrl();
    }

    public void deleteFile(Long id) throws Exception {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FileEntity file = fileRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("File not found"));

        cloudinary.uploader().destroy(
                file.getPublicId(),
                ObjectUtils.emptyMap()
        );

        fileRepository.delete(file);
    }
    private FileResponse mapToResponse(FileEntity file) {

        return FileResponse.builder()
                .id(file.getId())
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .fileSize(file.getFileSize())
                .fileUrl(file.getFileUrl())
                .uploadedAt(file.getUploadedAt())
                .build();
    }
}