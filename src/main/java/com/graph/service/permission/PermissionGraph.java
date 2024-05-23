package com.graph.service.permission;

import java.util.List;

import com.graph.dto.ResourcePermission;
import com.graph.model.permission.RoleAssignment;

public interface PermissionGraph {
    public PermissionGraph addResourcePermission(ResourcePermission resourcePermission);
    public List<RoleAssignment> getRoleAssignments(String identity, String resourceId);
    public List<RoleAssignment> getAllRoleAssignments(String identity);
}
