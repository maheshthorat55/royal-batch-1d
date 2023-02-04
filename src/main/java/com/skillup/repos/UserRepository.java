package com.skillup.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Page<User> findAllByDisplayUserId(Pageable pageable, String userName);

	List<User> findAllByType(String type);

	Optional<User> findByUserNameAndPassword(String userName, String passWord);

	Optional<User> findByDisplayUserIdAndPassword(String displayUserId, String passWord);

	User findByDisplayUserId(String displayUserId);

	List<User> findAllByDisplayUserIdStartsWith(String displayUserId);

	Page<User> findAllByReportingUserAndUserName(Pageable paging, User reportingUser, String userName);

	Page<User> findAllByReportingUser(Pageable paging, User byId);

	List<User> findAllByReportingUser(User reportingUser);

	List<User> findAllByReportingUserAndDisplayUserIdStartsWith(User byId, String displayUser);

	Page<User> findAllByUserName(Pageable pageable, String userName);

	Page<User> findAllByReportingUserAndDisplayUserId(Pageable paging, User byId, String userName);

}
