package com.graph.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.graph.model.Node;
import com.graph.model.Relation;
import com.graph.service.permission.RelationTypes;

public class GraphService implements GraphOps {
    
    Map<String, Node> nodes = new HashMap<>();
    Map<String, Set<Relation>> edges = new HashMap<>();

    @Override
    public Node addNode(String id, Object value, String type) {
        Node node = this.nodes.get(id);
        if (node == null) {
            node = new Node(id, value, id);
            this.nodes.put(id, node);
        } else {
            node.setValue(value);
        }
        return node;
    }

    @Override
    public Relation addRelation(String from, String to, Object value, RelationTypes type) {
        Relation relation = new Relation(from, to, type, value);
        Set<Relation> relations = this.edges.computeIfAbsent(relation.getKey(), k -> new HashSet<>());
        relations.add(relation);
        if (relations.size() == 1) {
            this.nodes.get(from).addOutGoing(relation);
            this.nodes.get(to).addIncoming(relation);
        }
        return relation;
    }

    @Override
    public Node getNode(String id) {
        return this.nodes.get(id);
    }

    @Override
    public Set<Relation> findRelations(String from, String to, RelationTypes type) {
        Relation relation = new Relation(from, to, type, null);
        return this.edges.get(relation.getKey());
    }

    private void print(List<Node> path) {
        path.stream().forEach(n -> System.out.println(n.getId()));
    }

    @Override
    public List<Node> findShortestPath(String src, String dst, RelationTypes ofType) {
        List<Node> path = new ArrayList<>();
        Node srcNode = this.getNode(src);
        path.add(srcNode);
        if (src.equals(dst)) {
            return path;
        }
        Map<String, List<Relation>> outGoingRelations = srcNode.getOutGoingRelations(ofType);
        if (outGoingRelations == null) {
            return null;
        }
        List<Node> selectedPath = null;
        for (String child: outGoingRelations.keySet()) {
            List<Node> pathFromChild = this.findShortestPath(child, dst, ofType);
            if (pathFromChild != null) {
                if (selectedPath == null) {
                    selectedPath = pathFromChild;
                } else {
                    selectedPath = (selectedPath.size() > pathFromChild.size()) ? pathFromChild : selectedPath;
                }
            }
        }
        if (selectedPath == null) {
            return null;
        }
        path.addAll(selectedPath);
        print(path);
        return path;
    }

    @Override
    public List<Node> getNodeHirerchy(String nodeId, RelationTypes type) {
        List<Node> hirerchy = new ArrayList<>();
        while (nodeId != null) {
            Node node = this.getNode(nodeId);
            hirerchy.add(node);
            nodeId = getResourceParent(nodeId, type);
        }
        
        return hirerchy;
    }

    protected String getResourceParent(String nodeId, RelationTypes type) {
        Node node = this.getNode(nodeId);
        Map<String, List<Relation>> incomingRelations = node.getIncomingRelations(type);
        Node parentResource = null;
        if (incomingRelations.size() > 0) {
            Relation[] parents = incomingRelations.values().toArray(new Relation[0]);
            Relation parent = parents[0];
            parentResource = new Node(parent.getFrom(), null, null);
        }
        return parentResource.getId();
    }
}
