package com.github.rlaehd62.entity.repository;

import com.github.rlaehd62.entity.file.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long>
{
    Optional<Profile> findByAccount_Id(String account);
}
