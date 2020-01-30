package com.swmansion.reanimated.nodes;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.swmansion.reanimated.NodesManager;
import com.swmansion.reanimated.UpdateContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import javax.annotation.Nullable;

public abstract class Node {
    public static final Double ONE = Double.valueOf(1.0d);
    public static final Double ZERO = Double.valueOf(0.0d);
    @Nullable
    private List<Node> mChildren;
    private long mLastLoopID = -1;
    @Nullable
    private Object mMemoizedValue;
    protected final int mNodeID;
    protected final NodesManager mNodesManager;
    private final UpdateContext mUpdateContext;

    /* access modifiers changed from: protected */
    @Nullable
    public abstract Object evaluate();

    public Node(int i, @Nullable ReadableMap readableMap, NodesManager nodesManager) {
        this.mNodeID = i;
        this.mNodesManager = nodesManager;
        this.mUpdateContext = nodesManager.updateContext;
    }

    @Nullable
    public final Object value() {
        if (this.mLastLoopID >= this.mUpdateContext.updateLoopID) {
            return this.mMemoizedValue;
        }
        this.mLastLoopID = this.mUpdateContext.updateLoopID;
        Object evaluate = evaluate();
        this.mMemoizedValue = evaluate;
        return evaluate;
    }

    public final Double doubleValue() {
        Object value = value();
        if (value == null) {
            return ZERO;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? ONE : ZERO;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Value of node ");
        sb.append(this);
        sb.append(" cannot be cast to a number");
        throw new IllegalStateException(sb.toString());
    }

    public void addChild(Node node) {
        if (this.mChildren == null) {
            this.mChildren = new ArrayList();
        }
        this.mChildren.add(node);
        dangerouslyRescheduleEvaluate();
    }

    public void removeChild(Node node) {
        List<Node> list = this.mChildren;
        if (list != null) {
            list.remove(node);
        }
    }

    /* access modifiers changed from: protected */
    public void markUpdated() {
        UiThreadUtil.assertOnUiThread();
        this.mUpdateContext.updatedNodes.add(this);
        this.mNodesManager.postRunUpdatesAfterAnimation();
    }

    /* access modifiers changed from: protected */
    public final void dangerouslyRescheduleEvaluate() {
        this.mLastLoopID = -1;
        markUpdated();
    }

    /* access modifiers changed from: protected */
    public final void forceUpdateMemoizedValue(Object obj) {
        this.mMemoizedValue = obj;
        markUpdated();
    }

    private static void findAndUpdateNodes(Node node, Set<Node> set, Stack<FinalNode> stack) {
        if (!set.contains(node)) {
            set.add(node);
            List<Node> list = node.mChildren;
            if (list != null) {
                for (Node findAndUpdateNodes : list) {
                    findAndUpdateNodes(findAndUpdateNodes, set, stack);
                }
            }
            if (node instanceof FinalNode) {
                stack.push((FinalNode) node);
            }
        }
    }

    public static void runUpdates(UpdateContext updateContext) {
        UiThreadUtil.assertOnUiThread();
        ArrayList<Node> arrayList = updateContext.updatedNodes;
        HashSet hashSet = new HashSet();
        Stack stack = new Stack();
        for (int i = 0; i < arrayList.size(); i++) {
            findAndUpdateNodes((Node) arrayList.get(i), hashSet, stack);
            if (i == arrayList.size() - 1) {
                while (!stack.isEmpty()) {
                    ((FinalNode) stack.pop()).update();
                }
            }
        }
        arrayList.clear();
        updateContext.updateLoopID++;
    }
}
