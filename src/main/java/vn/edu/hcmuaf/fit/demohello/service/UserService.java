package vn.edu.hcmuaf.fit.demohello.service;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import vn.edu.hcmuaf.fit.demohello.dto.request.UserRequestDTO;
import vn.edu.hcmuaf.fit.demohello.dto.response.PageResponse;
import vn.edu.hcmuaf.fit.demohello.dto.response.UserDetailResponse;
import vn.edu.hcmuaf.fit.demohello.model.User;
import vn.edu.hcmuaf.fit.demohello.utils.UserStatus;

import java.util.List;

public interface UserService {

    UserDetailsService userDetailsService();

    User getByUsername(String userName);

    long saveUser(UserRequestDTO request);

    void updateUser(long userId, UserRequestDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);

    PageResponse<?> getAllUsersWithSortByColumnsAndSearch(int pageNo, int pageSize, String search, String sortBy);

    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String address, String... search);

    PageResponse<?> advanceSearchBySpecification(Pageable pageable, String[] user, String[] address);

    List<String> findAllRolesByUserId(long userId);
}
