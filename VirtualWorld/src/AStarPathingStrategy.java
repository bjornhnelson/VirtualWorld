import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy  {

    public List<Point> computePath(Point start, Point end,
                                          Predicate<Point> canPassThrough,
                                          BiPredicate<Point, Point> withinReach,
                                          Function<Point, Stream<Point>> potentialNeighbors) {
        Map<Point, Node> openList = new HashMap<>();
        Map<Point, Node> closedList = new HashMap<>();

        Node current = new Node(start, 0, heuristicDistance(start, end), heuristicDistance(start, end), null);
        openList.put(current.getPosition(), current);

        while (openList.size() > 0) {

            Node minF = null;
            for (Node n : openList.values()) {
                if (minF == null)
                    minF = n;
                else if (n.getFScore() < minF.getFScore())
                    minF = n;
            }
            current = minF;

            if (withinReach.test(current.getPosition(), end))
                return reconstructPath(closedList, current.getPosition());

            openList.remove(current);
            closedList.put(current.getPosition(), current);

            List<Point> adjacentPoints = potentialNeighbors.apply(current.getPosition()).filter(canPassThrough).collect(Collectors.toList());
            for (Point p : adjacentPoints) {

                if (closedList.containsKey(p))
                    continue;

                for (Node n : openList.values()) {
                    if (!openList.containsValue(n))
                        continue;

                    else {
                        Integer tentativeGScore = current.getGScore() + heuristicDistance(current.getPosition(), p);
                        if (tentativeGScore > heuristicDistance(start, p))
                            continue;
                        Point a = p;
                        Integer b = tentativeGScore;
                        Integer c = heuristicDistance(p, end);
                        Integer d = b + c;
                        Point e = current.getPosition();

                        Node addedNode = new Node(a, b, c, d, e);
                        openList.put(a, addedNode);
                    }
                }

            }
        }
        return null;
    }

    public static int heuristicDistance(Point p1, Point p2) {
        int distance = Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
        return distance;
    }

    public List<Point> reconstructPath(Map<Point, Node> closedList, Point current) {
        List<Point> result = new LinkedList<>();
        boolean continueRun = true;

        while (continueRun) {
            if (current == null)
                continueRun = false;
            else
                result.add(0, current);

            Point previousKey = closedList.get(current).getPrevious();
            current = closedList.get(previousKey).getPosition();

            }
        return result;
    }

}
