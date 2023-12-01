package com.example.excutor.api.service;

import com.example.excutor.api.dao.UserDao;
import com.example.excutor.api.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    Object target;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {
        long startTime = System.currentTimeMillis();

        List<User> users = parseCSVFile(file);
        logger.info("saving list of users of size {}", users.size() + " " + Thread.currentThread().getName(), "{}" + Thread.currentThread().getName());
        users = userDao.saveAll(users);
        long endTime = System.currentTimeMillis();
        logger.info("Total time {}", (endTime - startTime));
        return CompletableFuture.completedFuture(users);
    }

    public List<User> saveUsersNo(MultipartFile file) throws Exception {
        long startTime = System.currentTimeMillis();
        List<User> users = parseCSVFile(file);
        logger.info("saving list of users of size {}", users.size(), "{}" + Thread.currentThread().getName());
        users = userDao.saveAll(users);
        long endTime = System.currentTimeMillis();
        logger.info("Total time {}", (endTime - startTime));
        return users;
    }

    @Async
    public CompletableFuture<List<User>> findAllUsers() {
        long startTime = System.currentTimeMillis();
        logger.info("get list of users by " + Thread.currentThread().getName());
        List<User> users = userDao.findAll();
        long endTime = System.currentTimeMillis();
        logger.info("Total time {}", (endTime - startTime));
        return CompletableFuture.completedFuture(users);
    }

    public List<User> findAllClassic() {
        long startTime = System.currentTimeMillis();
        List<User> users = userDao.findAll();
        long endTime = System.currentTimeMillis();
        logger.info("Total time {}", (endTime - startTime));
        return users;
    }

    private List<User> parseCSVFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setName(data[0]);
                    user.setEmail(data[1]);
                    user.setGender(data[2]);
                    users.add(user);
                }
                return users;
            }
        } catch (final IOException e) {
            logger.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
}
