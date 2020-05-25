package application.data.service;

import application.data.entity.UserRole;
import application.data.repository.UserRoleRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    public static final Logger logger = LogManager.getLogger(UserRoleService.class) ;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public void addNewUserRole(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    public UserRole findUserRolebyRoleIdAndUserId(int roleId, int userId) {
        return userRoleRepository.findUserRolebyRoleIdAndUserId(roleId, userId);
    }

    public Page<UserRole> findUserRolebyRoleId(Pageable pageable, int roleId) {
        return userRoleRepository.findUserRolebyRoleId(pageable, roleId);
    }

    public Page<UserRole> getAllUserRole(Pageable pageable) {
        return userRoleRepository.getAllUserRole(pageable);
    }

    public boolean deleteUserRole(int userId) {
        try {
            List<UserRole> userRoleList = userRoleRepository.findUserRolebyUserId(userId);
            for(UserRole userRole : userRoleList) {
                userRoleRepository.delete(userRole.getId());
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
