package com.github.rlaehd62.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rlaehd62.entity.auth.Role;

public interface RoleRepository extends JpaRepository<Role, Long>
{

}
