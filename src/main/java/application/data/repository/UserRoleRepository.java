package application.data.repository;

import application.data.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Transactional(readOnly = true)
    @Query("select u from dbo_user_role u where u.userId = :id")
    Iterable<UserRole> findRolesOfUser(@Param("id") int userId);


    @Query("select u from dbo_user_role u where u.roleId = :roleId and u.userId = :userId")
    UserRole findUserRolebyRoleIdAndUserId(@Param("roleId") int roleId, @Param("userId") int userId);

    @Query("select u from dbo_user_role u where u.roleId = :roleId")
    Page<UserRole> findUserRolebyRoleId(Pageable pageable, @Param("roleId") int roleId);

    @Query("select u from dbo_user_role u where u.userId = :userId")
    List<UserRole> findUserRolebyUserId(@Param("userId") int userId);

    @Query("select u from dbo_user_role u")
    Page<UserRole> getAllUserRole(Pageable pageable);
}
