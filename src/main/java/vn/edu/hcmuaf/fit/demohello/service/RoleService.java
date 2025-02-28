package vn.edu.hcmuaf.fit.demohello.service;

import jakarta.annotation.PostConstruct;
import vn.edu.hcmuaf.fit.demohello.model.Role;
import vn.edu.hcmuaf.fit.demohello.repository.RoleRepository;

import java.util.List;

public record RoleService(RoleRepository roleRepository) {

    @PostConstruct
    public List<Role> findAll() {

        List<Role> roles = roleRepository.findAll();

        return roles;
    }
}
