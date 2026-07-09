package com.shreyas.securefilesharing.repository;

import com.shreyas.securefilesharing.entity.FileEntity;
import com.shreyas.securefilesharing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByUser(User user);

    Optional<FileEntity> findByIdAndUser(Long id, User user);
}