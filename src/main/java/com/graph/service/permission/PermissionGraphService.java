package com.graph.service.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.graph.dto.Binding;
import com.graph.dto.ResourcePermission;
import com.graph.model.Node;
import com.graph.model.Relation;
import com.graph.model.permission.Identity;
import com.graph.model.permission.NodeTypes;
import com.graph.model.permission.Resource;
import com.graph.model.permission.Role;
import com.graph.model.permission.RoleAssignment;
import com.graph.service.GraphService;

public class PermissionGraphService extends GraphService implements PermissionGraph {

    
    @Override
    public PermissionGraph addResourcePermission(ResourcePermission resourcePermission) {
        Resource resource = new Resource(resourcePermission.getName(), resourcePermission.getAssetType());
        this.addNode(resourcePermission.getName(), resource, NodeTypes.RESOURCE);
        handleAncestors(resourcePermission, resource);
        handleBindings(resourcePermission, resource);
        return this;
    }

    private void handleBindings(ResourcePermission resourcePermission, Resource resource) {
        for (Binding binding: resourcePermission.getBindings()) {
            List<String> members = binding.getMembers();
            for (String member: members) {
                Identity memberIdentity  = new Identity(member);
                this.addNode(member, memberIdentity, NodeTypes.IDENTITY);
                //String from, String to, String typ, Object value
                Role role = new Role(binding.getRole());
                RoleAssignment roleAssignment = new RoleAssignment(resource.getId(), role);
                this.addRelation(member, resource.getId(), roleAssignment, RelationTypes.ROLE_ASSIGNMENT);
            }
        }
    }

    private void handleAncestors(ResourcePermission resourcePermission, Resource resource) {
        List<Resource> ancestors = resourcePermission.getAncestors().stream().map(s -> getResource(s)).collect(Collectors.toList());
        Resource child = resource;
        for (Resource ancestor: ancestors) {
            this.addNode(ancestor.getId(), ancestor, NodeTypes.RESOURCE);
            this.addRelation(ancestor.getId(), child.getId(), null, RelationTypes.RESOURCE_PARENT);
            child = ancestor;
        }
    }

    private Resource getResource(String id) {
        Resource resource = new Resource(id, null);
        return resource;
    }

    @Override
    public List<RoleAssignment> getRoleAssignments(String identity, String resourceId) {
        Set<Relation> relations = null;
        while (relations == null && resourceId != null) {
            relations = this.findRelations(identity, resourceId, RelationTypes.ROLE_ASSIGNMENT);
            if (relations == null) {
                resourceId = getResourceParent(resourceId, RelationTypes.RESOURCE_PARENT);
            }
        }
        
        List<RoleAssignment> roleAssignments = null;
        if  (relations != null && !relations.isEmpty()) {
            roleAssignments = relations.stream().map(r -> (RoleAssignment) r.getValue()).collect(Collectors.toList());
        };
        return roleAssignments;
    }


    @Override
    public List<RoleAssignment> getAllRoleAssignments(String identity) {
        Node node = this.getNode(identity);
        Map<String, List<Relation>> roleAssignments = node.getOutGoingRelations(RelationTypes.ROLE_ASSIGNMENT);
        List<RoleAssignment> finalResult = new ArrayList<>();
        for (List<Relation> relations: roleAssignments.values()) {
            relations.stream().forEach(relation -> finalResult.add((RoleAssignment)relation.getValue()));
        }
        return finalResult;
    }

    
}
