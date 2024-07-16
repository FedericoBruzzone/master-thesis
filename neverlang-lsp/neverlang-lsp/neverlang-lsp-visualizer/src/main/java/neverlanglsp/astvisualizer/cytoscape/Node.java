package neverlanglsp.astvisualizer.cytoscape;

public record Node(String id, String label, boolean terminal) implements TreeElement {}
