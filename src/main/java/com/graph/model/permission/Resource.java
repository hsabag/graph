package com.graph.model.permission;



public class Resource {

    
    private String resourceType;
    private String id;

    public String getId() {
        return id;
    }

    public Resource(String id, String resourceType) {
        this.id = id;
        this.resourceType = resourceType;
    }
    
}
