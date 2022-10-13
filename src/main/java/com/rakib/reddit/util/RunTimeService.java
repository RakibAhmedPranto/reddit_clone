package com.rakib.reddit.util;

import com.rakib.reddit.model.Role;
import com.rakib.reddit.repository.RoleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.rakib.reddit.util.Constants.ROLE_ADMIN;
import static com.rakib.reddit.util.Constants.ROLE_USER;

@Service
public class RunTimeService {
    @Autowired
    private RoleRepository roleRepository;


    public void createRuntimeRoleEntities() throws Exception {
        try {
            Role role1 = new Role();
            role1.setId(ROLE_ADMIN);
            role1.setName("ROLE_ADMIN");

            Role role2 = new Role();
            role2.setId(ROLE_USER);
            role2.setName("ROLE_USER");

            List<Role> roles = List.of(role1, role2);
            this.roleRepository.saveAll(roles);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
