package com.graph.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.graph.dto.Binding;
import com.graph.dto.ResourcePermission;
import com.graph.model.Node;
import com.graph.model.Relation;
import com.graph.model.permission.RoleAssignment;
import com.graph.service.permission.PermissionGraphService;
import com.graph.service.permission.RelationTypes;


public class PermissionGraphTest {

    @Test
    public void testGraph() throws IOException {

        PermissionGraphService graphService = new PermissionGraphService();

        
        ResourcePermission resourcePermission = new ResourcePermission();
        resourcePermission.setName("folders/188906894377");
        resourcePermission.setAssetType("Folder");
        List<Binding> bindings = new ArrayList<>();
        Binding binding1 = new Binding();
        binding1.setRole("roles/owner");
        binding1.setMembers(List.of("serviceAccount1", "serviceAccount2"));
        bindings.add(binding1);
        Binding binding2 = new Binding();
        binding2.setRole("roles/resourcemanager.folderAdmin");
        binding2.setMembers(List.of("user1"));
        bindings.add(binding2);
        Binding binding3 = new Binding();
        binding3.setRole("roles/resourcemanager.folderEditor");
        binding3.setMembers(List.of("user1"));
        bindings.add(binding3);
        resourcePermission.setBindings(bindings);
        resourcePermission.setAncestors(List.of("folders/1", "folders/2", "orgs/1"));
        graphService.addResourcePermission(resourcePermission);

        Node mainNode = graphService.getNode("folders/188906894377");
        Assert.assertNotNull(mainNode);

        Set<Relation> parent = graphService.findRelations("folders/1", "folders/188906894377", RelationTypes.RESOURCE_PARENT);
        Assert.assertFalse(parent.isEmpty());

        List<RoleAssignment> roleAssignments = graphService.getRoleAssignments("user1", "folders/188906894377");
        Assert.assertEquals(roleAssignments.size(), 2);

        List<Node> path = graphService.findShortestPath("user1", "folders/188906894377", null);
        Assert.assertEquals(2, path.size());

        List<Node> rootPath = graphService.findShortestPath("orgs/1", "folders/188906894377", null);
        Assert.assertEquals(4, rootPath.size());
    }

    

}

