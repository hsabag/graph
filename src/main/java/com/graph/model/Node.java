package com.graph.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.graph.service.permission.RelationTypes;

final public class Node {
    String id;
    String type;
    Object value;
    
    Map<RelationTypes, Map<String, List<Relation>>> outGoingRelations = new HashMap<>();
    Map<RelationTypes, Map<String, List<Relation>>> incomingRelations = new HashMap<>();

    public Map<String, List<Relation>> getOutGoingRelations(RelationTypes type) {
        if (type != null) {
            return outGoingRelations.get(type);
        } else {
            Map<String, List<Relation>> finalResult = new HashMap<>();
            outGoingRelations.values().stream().forEach(v -> finalResult.putAll(v));
            return finalResult;
        }
    }

    

    public Map<String, List<Relation>> getIncomingRelations(RelationTypes type) {
        return incomingRelations.get(type);
    }

    public Node(String id, Object value, String type) {
        this.id = id;
        this.type = type;
        this.value = value;
    }
    
    public String getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object newValue) {
        this.value = newValue;
    }

    public void addOutGoing(Relation relation) {
        Map<String, List<Relation>> typeRelations = this.outGoingRelations.computeIfAbsent(relation.type, type -> new HashMap<>());
        List<Relation> relationsList = typeRelations.computeIfAbsent(relation.to, id -> new ArrayList<>());
        relationsList.add(relation);
    }

    public void addIncoming(Relation relation) {
        Map<String, List<Relation>> typeRelations = this.incomingRelations.computeIfAbsent(relation.type, type -> new HashMap<>());
        List<Relation> relationsList = typeRelations.computeIfAbsent(relation.to, id -> new ArrayList<>());
        relationsList.add(relation);
    }
}
