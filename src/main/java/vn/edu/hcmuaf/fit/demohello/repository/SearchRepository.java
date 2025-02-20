package vn.edu.hcmuaf.fit.demohello.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vn.edu.hcmuaf.fit.demohello.dto.response.PageResponse;
import vn.edu.hcmuaf.fit.demohello.model.Address;
import vn.edu.hcmuaf.fit.demohello.model.User;
import vn.edu.hcmuaf.fit.demohello.repository.criteria.SearchCriteria;
import vn.edu.hcmuaf.fit.demohello.repository.criteria.UserSearchCriteriaQueryConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUsersWithSortByColumnsAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        StringBuilder sqlQuery = new StringBuilder("select new vn.edu.hcmuaf.fit.demohello.dto.response.UserDetailResponse(" +
                "u.id, u.firstName, u.lastName, u.email, u.phone) from User u where 1 = 1");
        if(StringUtils.hasLength(search)) {
            sqlQuery.append(" and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.lastName) like lower(:lastName)");
            sqlQuery.append(" or lower(u.email) like lower(:email)");
        }

        if(StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                sqlQuery.append(String.format(" order by u.%s %s", matcher.group(1), matcher.group(3)));
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        if(StringUtils.hasLength(search)) {
            selectQuery.setParameter("firstName", String.format("%%%s%%", search));
            selectQuery.setParameter("lastName", String.format("%%%s%%", search));
            selectQuery.setParameter("email", String.format("%%%s%%", search));
        }
        List users = selectQuery.getResultList();
        System.out.println(users);

        // query ra list user
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from User u where 1 = 1");
        if(StringUtils.hasLength(search)) {
            sqlCountQuery.append(" and lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" and lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" and lower(u.email) like lower(?3)");
        }

        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if(StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
            selectCountQuery.setParameter(3, String.format("%%%s%%", search));
        }

        Long totalElements = (Long) selectCountQuery.getSingleResult();
        System.out.println(totalElements);

        Page<?> page = new PageImpl<Object>(users, PageRequest.of(pageNo, pageSize) , totalElements);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(page.stream().toList())
                .build();
    }

    public PageResponse advanceSearchUser(int pageNo, int pageSize, String sortBy, String address, String...search) {
        // firstName:T , lastName:T

        List<SearchCriteria> criteriaList = new ArrayList<>();
        // 1. lay ra danh sach user
        if(search != null) {
            for(String s : search) {
                // firstName:value --> group(1) group(2) group(3)
                Pattern pattern = Pattern.compile("(\\w+?)([:><])(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        // 2. lay ra so luong ban ghi
        List<User> users = getUsers(pageNo, pageSize, criteriaList, sortBy, address);

        Long totalElements = getTotalElements(criteriaList, address);

        return PageResponse.builder()
                .pageNo(pageNo)     // offset = vị trí bản ghi trong danh sách
                .pageSize(pageSize)
                .totalPage(totalElements.intValue()) // totalElements
                .items(users)
                .build();
    }

    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy, String address) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // Xử lý các điều kiện tìm kiếm
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);

        if(StringUtils.hasLength(address)) {
            Join<Address, User> addressUserJoin = root.join("addresses");
            Predicate addressPredicate = criteriaBuilder.like(addressUserJoin.get("city"), "%"+address+"%");
            query.where(predicate, addressPredicate);
        } else {
            criteriaList.forEach(queryConsumer);
            predicate = queryConsumer.getPredicate();
            query.where(predicate);
        }
        // sort
        if(StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                String columnName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                }else {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }
            }
        }

        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }
    private Long getTotalElements(List<SearchCriteria> criteriaList, String address) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer searchConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);
        if(StringUtils.hasLength(address)) {
            Join<Address, User> addressUserJoin = root.join("addresses");
            Predicate addressPredicate = criteriaBuilder.like(addressUserJoin.get("city"), "%"+address+"%");
            query.where(predicate, addressPredicate);
        } else {
            criteriaList.forEach(searchConsumer);
            predicate = searchConsumer.getPredicate();
        }
        query.where(predicate);
        query.select(criteriaBuilder.count(root));
        return entityManager.createQuery(query).getSingleResult();
    }
}
