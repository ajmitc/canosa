package canosa.game.board;

import canosa.game.board.Board;
import canosa.game.board.Cell;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AStarAlgorithm {
    public static List<Cell> findShortestPath(Cell start, Cell end, Board board){
        return findShortestPath(start, end, board, false);
    }

    public static List<Cell> findShortestPath(Cell start, Cell end, Board board, boolean mustMoveTowardEnd){
        Map<Cell, Node> locationNodes = new HashMap<>();
        board.getCells().stream().forEach(l -> locationNodes.put(l, new Node(l)));

        // Initialize the open list
        Queue<Node> openList = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.f < o2.f? 1: (o1.f > o2.f? -1: 0);
            }
        });
        // Initialize the closed list (visited)
        List<Node> visited = new ArrayList<>();

        // put the starting node on the open list (you can leave its f at zero)
        Node startNode = new Node(start);
        startNode.f = 0;
        openList.add(startNode);

        boolean incDiagonal = start.getPiece() != null && start.getPiece().getType().isSiren();

        // while the open list is not empty
        while (!openList.isEmpty()) {
            Node current = openList.remove();

            if (!visited.contains(current)){
                visited.add(current);

                if (current.location == end)
                    return reconstructPath(startNode, current, board);

                List<Node> neighbors =
                        board.getNeighboringCells(current.location, incDiagonal).stream()
                                .map(cell -> locationNodes.values().stream().filter(node -> node.location == cell).findFirst().get())
                                .collect(Collectors.toList());

                for (Node neighbor : neighbors) {
                    if (!visited.contains(neighbor)){
                        // increment hops from start
                        neighbor.hopsFromStart = current.hopsFromStart + 1;

                        // calculate predicted distance to the end node
                        int predictedDistance = calcEstimatedCostToMove(neighbor.location, end, current.location, board, mustMoveTowardEnd);

                        if (predictedDistance > 90){
                            // If we returned a very large cost, this path isn't available
                            continue;
                        }

                        // calculate distance to neighbor. 2. calculate dist from start node
                        int totalDistance = calcActualCostToMove(startNode, neighbor) + predictedDistance;

                        // update n's distance
                        neighbor.f = totalDistance;

                        // if a node with the same position as successor is in the OPEN list which has a lower f than successor, skip this successor
                        Optional<Node> betterNode = openList.stream().filter(n -> n.hopsFromStart == neighbor.hopsFromStart && n.f < neighbor.f).findFirst();
                        if (betterNode.isPresent())
                            continue;

                        // if a node with the same position as successor is in the CLOSED list which has a lower f than successor, skip this successor
                        betterNode = visited.stream().filter(n -> n.hopsFromStart == neighbor.hopsFromStart && n.f < neighbor.f).findFirst();
                        if (betterNode.isPresent())
                            continue;

                        // otherwise, add  the node to the open list
                        neighbor.parent = current;
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private static List<Cell> reconstructPath(Node startNode, Node endNode, Board board){
        List<Cell> path = new ArrayList<>();
        Node current = endNode;
        while (current.location != startNode.location){
            Node parent = current.parent;
            final Cell currentLocation = current.location;
            path.add(currentLocation);
            current = parent;
        }
        Collections.reverse(path);
        return path;
    }

    // Return g
    private static int calcActualCostToMove(Node location1, Node location2){
        int cost = location2.hopsFromStart - location1.hopsFromStart;
        return cost;
    }

    // Return h
    private static int calcEstimatedCostToMove(Cell location1, Cell location2, Cell current, Board board, boolean mustMoveTowardEnd){
        if (location2.getPiece() != null || location2.getIslandOwner() != null)
            return 99;
        if (mustMoveTowardEnd){
            int d1 = getDistanceBetween(current, location2);
            int d2 = getDistanceBetween(location1, location2);
            if (d2 > d1)
                return 99;
        }
        return 1;
    }

    public static int getDistanceBetween(Cell cell1, Cell cell2){
        int x1 = cell1.getX();
        int x2 = cell2.getX();
        int y1 = cell1.getY();
        int y2 = cell2.getY();
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    static class Node{
        public Cell location;
        public Node parent = null;
        public int hopsFromStart = 0;
        public int f = Integer.MAX_VALUE;

        public Node(Cell l){
            this.location = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return location.equals(node.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location);
        }
    }
}