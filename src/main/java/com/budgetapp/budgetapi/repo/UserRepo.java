package com.budgetapp.budgetapi.repo;

import com.budgetapp.budgetapi.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users,Integer> {

    Users findByUsername(String username);

}
