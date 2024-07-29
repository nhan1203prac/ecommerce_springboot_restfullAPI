package com.project.backend.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.model.Picture;

public interface PictureRespository extends JpaRepository<Picture, Long>{

}
