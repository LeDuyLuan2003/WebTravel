package com.SpringBootJdk22.SpringBootJdk22.repository;

import com.SpringBootJdk22.SpringBootJdk22.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long>{
    Role findRoleById(Long id);
}

