package application.authen;


import application.data.entity.Role;
import application.data.entity.User;
import application.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userDetailsService")
public class MySQLUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if(user != null) {
            List<Role> listActiveRoles = userService.getActiveListRole(user.getId());
            boolean isActive =true;

            if(listActiveRoles.size() == 0) {
                isActive = false;
            }

            Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
            for(Role role : listActiveRoles) {
                setAuths.add(new SimpleGrantedAuthority(role.getName()));
            }

            org.springframework.security.core.userdetails.User userDetail =
                    new org.springframework.security.core.userdetails.User(username, user.getPasswordHash(),
                            isActive, true, true, true, setAuths);

            return userDetail;
        }
        throw new UsernameNotFoundException(username + "Không tìm thấy!");
    }
}
