package com.example.excutor.api.ws;

import com.example.excutor.api.entity.User;
import com.example.excutor.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam("files") List<MultipartFile> files) throws Exception {
        for (MultipartFile file : files) {
            userService.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @GetMapping(value = "/", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
        CompletableFuture<List<User>> users = userService.findAllUsers();
        return users.thenApply(ResponseEntity::ok);
    }

    @GetMapping("/getUsersByMultThreads")
    public ResponseEntity getUsers() {
        long startTime = System.currentTimeMillis();
        CompletableFuture<List<User>> users1 = userService.findAllUsers();
        CompletableFuture<List<User>> users2 = userService.findAllUsers();
        CompletableFuture<List<User>> users3 = userService.findAllUsers();
        long endTime = System.currentTimeMillis();
        logger.info("Total time {}", (endTime - startTime));
        CompletableFuture.allOf(users1, users2, users3).join(); // once is the three Thread is finish it will join
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/classic-save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsersNo(@RequestParam("files") List<MultipartFile> files) throws Exception {
        for (MultipartFile file : files) {
            userService.saveUsersNo(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/classic-get")
    public ResponseEntity<List<User>> findAllClassic(){
        return ResponseEntity.ok(userService.findAllClassic());
    }
}
