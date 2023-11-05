package ra.security.service.impl.usersevice;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ra.security.exception.LoginException;

@Service
public class UserServiceOnline {

    public String getCurrentUsername() {
        // Lấy principal từ Authentication object trong SecurityContextHolder
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal != null) {

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return userDetails.getUsername();
            } else {

                return null;
            }
        } else {

            return null;
        }
    }
    }