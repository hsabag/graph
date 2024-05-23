package com.graph.dto;

import java.util.List;

public class ResourcePermission {
    private String name;
    private String assetType;
    private List<Binding> bindings;
    private List<String> ancestors;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAssetType() {
        return assetType;
    }
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
    
    public List<Binding> getBindings() {
        return bindings;
    }
    public void setBindings(List<Binding> bindings) {
        this.bindings = bindings;
    }
    
    public List<String> getAncestors() {
        return ancestors;
    }
    public void setAncestors(List<String> ancestors) {
        this.ancestors = ancestors;
    }
}
