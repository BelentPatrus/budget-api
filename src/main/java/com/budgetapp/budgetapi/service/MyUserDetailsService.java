package com.budgetapp.budgetapi.service;

import com.budgetapp.budgetapi.model.user.Users;
import com.budgetapp.budgetapi.model.user.UserPrincipal;
import com.budgetapp.budgetapi.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if (user != null) {
            return new UserPrincipal(user);
        }
        throw new UsernameNotFoundException("User not found");
    }
}
