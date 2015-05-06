package edu.wdaniels.lg.structures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author William
 */
public class DepthFirstSearch {

    List<TreeNode> visitedList = new ArrayList<>();
    boolean foundNewRoute;

    public boolean dfs(TreeNode<Pair<Triple<Integer, Integer, Integer>, Boolean>> inputRoot) {
        visitedList.clear();
        foundNewRoute = false;
        TreeNodeIter<TreeNode> iter = new TreeNodeIter(inputRoot);
        TreeNode<Pair<Triple<Integer, Integer, Integer>, Boolean>> current = inputRoot;
        if (current.children.isEmpty()) {
            return false;
        }
        while (!foundNewRoute && iter.hasNext()) {
            if (current.isLeaf() && current.data.getSecond()) {
                current = inputRoot;
            }
            for (TreeNode<Pair<Triple<Integer, Integer, Integer>, Boolean>> child : current.children) {
                if (!visitedList.contains(child)) {
                    visitedList.add(child);
                    if (!child.data.getSecond()) {
                        return true;
                    }
                }
            }
            iter.next();
        }
        return false;
    }
}
