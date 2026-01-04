package com.budgetapp.budgetapi.util;

import com.budgetapp.budgetapi.model.user.UserPrincipal;
import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserVerify {

    private final UserRepo userRepo;

    @Autowired
    public UserVerify(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Users verifyUser(UserPrincipal principal) {
        String username = principal.getUsername();
        Users user = userRepo.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
