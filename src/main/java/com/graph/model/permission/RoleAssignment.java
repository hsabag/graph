package com.graph.model.permission;

public class RoleAssignment {

    private String resourceId;
    private Role role;

    public RoleAssignment(String resourceId, Role role) {
        this.resourceId = resourceId;
        this.role = role;
    }
}
