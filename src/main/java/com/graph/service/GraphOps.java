package com.graph.service;

import java.util.List;
import java.util.Set;

import com.graph.model.Node;
import com.graph.model.Relation;
import com.graph.service.permission.RelationTypes;

public interface GraphOps {
    public Node addNode(String id, Object value, String type);
    public Relation addRelation(String from, String to, Object value, RelationTypes type);
    public Node getNode(String id);
    public Set<Relation> findRelations(String from, String to, RelationTypes type);
    public List<Node> findShortestPath(String src, String dst, RelationTypes ofType);
    public List<Node> getNodeHirerchy(String nodeId, RelationTypes type);
}
