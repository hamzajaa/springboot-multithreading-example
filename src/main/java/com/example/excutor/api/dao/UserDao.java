package com.example.excutor.api.dao;

import com.example.excutor.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
