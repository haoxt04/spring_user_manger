package vn.edu.hcmuaf.fit.demohello.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.demohello.dto.request.UserRequestDTO;
import vn.edu.hcmuaf.fit.demohello.dto.response.ResponseData;
import vn.edu.hcmuaf.fit.demohello.dto.response.ResponseError;
import vn.edu.hcmuaf.fit.demohello.dto.response.UserDetailResponse;
import vn.edu.hcmuaf.fit.demohello.exception.ResourceNotfoundException;
import vn.edu.hcmuaf.fit.demohello.service.UserService;
import vn.edu.hcmuaf.fit.demohello.utils.UserStatus;

import java.util.List;


@RestController
@Validated
@Slf4j
@RequestMapping("/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Add user", description = "API create new user")
    @PostMapping
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO user) {
        log.info("Request add user = {} {}", user.getFirstName(), user.getLastName());
        try {
            long userId = userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "user added successful", userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Update user", description = "API update user info")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable long userId, @Valid @RequestBody UserRequestDTO userDto) {
        log.info("Request update userId = {}", userId);
        try {
            userService.updateUser(userId, userDto);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "user updated successful");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "update user fail");
        }
    }

    @Operation(summary = "Change status user", description = "API change status of user")
    @PatchMapping("/{userId}")
    public ResponseData<?> changeStatus(@PathVariable long userId,@Valid @RequestParam UserStatus status) {
        log.info("Request change user status, userId = {}", userId);
        try {
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "user updated successful");
        }catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "change status fail");
        }
    }

    @Operation(summary = "Delete user", description = "API delete user")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable long userId) {
        log.info("Request delete userId = {}" ,userId);
        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "user deleted successful");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "delete user fail");
        }
    }

    @Operation(summary = "Get user detail", description = "API get user detail")
    @GetMapping("/{userId}")
    public ResponseData<UserDetailResponse> getUser(@PathVariable long userId) {
        log.info("Request get user detail by userId = {}", userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", userService.getUser(userId));
        }catch (ResourceNotfoundException e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list user per page", description = "Return list user by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllUsers(@Valid @RequestParam(defaultValue = "0") int pageNo,
                                                              @Valid @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(required = false) String sortBy) {
        log.info("Request get user list");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get list of user with sort by multiple columns", description = "Return user by pageNo and pageSize, sort by multiple columns")
    @GetMapping("/list-with-sort-by-multiple-column")
    public ResponseData<?> getAllUsersWithSortByMultipleColumns(@Valid @RequestParam(defaultValue = "0") int pageNo,
                                                              @Valid @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(required = false) String... sort) {
        log.info("Request get list of user by multiple columns");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sort));
    }

    @Operation(summary = "Get list of users with sort by columns and search", description = "Return user by pageNo and pageSize, sort by multiple columns")
    @GetMapping("/list-with-sort-by-column-search")
    public ResponseData<?> getAllUsersWithSortByColumnsAndSearch(@Valid @RequestParam(defaultValue = "0") int pageNo,
                                                                @Valid @RequestParam(defaultValue = "10") int pageSize,
                                                                @RequestParam(required = false) String search,
                                                                 @RequestParam(required = false) String sortBy) {
        log.info("Request get list of user by columns and search");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortByColumnsAndSearch(pageNo, pageSize,search, sortBy));
    }

    @Operation(summary = "Get list of users and search with paging and sort with criteria", description = "Send a request via this API to get user list by pageNo, pageSize and advance search by criteria")
    @GetMapping("/advance-search-by-criteria")
    public ResponseData<?> advanceSearchByCriteria(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                   @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                   @RequestParam(required = false) String sortBy,
                                                   @RequestParam(required = false) String address,
                                                   @RequestParam(required = false) String... search) {

        log.info("Request advance search with paging and sorting");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.advanceSearchByCriteria(pageNo, pageSize, sortBy, address, search));
    }
}
