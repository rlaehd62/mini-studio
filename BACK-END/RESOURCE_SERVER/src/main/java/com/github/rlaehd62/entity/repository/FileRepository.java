package com.github.rlaehd62.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.File;

public interface FileRepository extends JpaRepository<File, Long>
{

}
