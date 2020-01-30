package com.swmansion.reanimated.nodes;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.UIImplementation;
import com.swmansion.reanimated.C1283Utils;
import com.swmansion.reanimated.NodesManager;
import java.util.Map;
import java.util.Map.Entry;

public class PropsNode extends Node implements FinalNode {
    private int mConnectedViewTag = -1;
    private final ReactStylesDiffMap mDiffMap;
    private final Map<String, Integer> mMapping;
    private final JavaOnlyMap mPropMap;
    private final UIImplementation mUIImplementation;

    public PropsNode(int i, ReadableMap readableMap, NodesManager nodesManager, UIImplementation uIImplementation) {
        super(i, readableMap, nodesManager);
        this.mMapping = C1283Utils.processMapping(readableMap.getMap("props"));
        this.mUIImplementation = uIImplementation;
        this.mPropMap = new JavaOnlyMap();
        this.mDiffMap = new ReactStylesDiffMap(this.mPropMap);
    }

    public void connectToView(int i) {
        this.mConnectedViewTag = i;
        dangerouslyRescheduleEvaluate();
    }

    public void disconnectFromView(int i) {
        this.mConnectedViewTag = -1;
    }

    /* access modifiers changed from: protected */
    public Double evaluate() {
        boolean z;
        WritableMap writableMap;
        WritableMap createMap = Arguments.createMap();
        WritableMap createMap2 = Arguments.createMap();
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        for (Entry entry : this.mMapping.entrySet()) {
            Node findNodeById = this.mNodesManager.findNodeById(((Integer) entry.getValue()).intValue(), Node.class);
            if (findNodeById instanceof StyleNode) {
                WritableMap writableMap2 = (WritableMap) findNodeById.value();
                ReadableMapKeySetIterator keySetIterator = writableMap2.keySetIterator();
                while (keySetIterator.hasNextKey()) {
                    String nextKey = keySetIterator.nextKey();
                    if (this.mNodesManager.uiProps.contains(nextKey)) {
                        writableMap = this.mPropMap;
                        z = true;
                    } else if (this.mNodesManager.nativeProps.contains(nextKey)) {
                        z = z2;
                        z3 = true;
                        writableMap = createMap2;
                    } else {
                        z = z2;
                        z4 = true;
                        writableMap = createMap;
                    }
                    ReadableType type = writableMap2.getType(nextKey);
                    switch (type) {
                        case Number:
                            writableMap.putDouble(nextKey, writableMap2.getDouble(nextKey));
                            break;
                        case String:
                            writableMap.putString(nextKey, writableMap2.getString(nextKey));
                            break;
                        case Array:
                            writableMap.putArray(nextKey, (WritableArray) writableMap2.getArray(nextKey));
                            break;
                        default:
                            StringBuilder sb = new StringBuilder();
                            sb.append("Unexpected type ");
                            sb.append(type);
                            throw new IllegalArgumentException(sb.toString());
                    }
                    z2 = z;
                }
                continue;
            } else {
                String str = (String) entry.getKey();
                if (this.mNodesManager.uiProps.contains(str)) {
                    this.mPropMap.putDouble(str, findNodeById.doubleValue().doubleValue());
                    z2 = true;
                } else {
                    createMap2.putDouble(str, findNodeById.doubleValue().doubleValue());
                    z3 = true;
                }
            }
        }
        int i = this.mConnectedViewTag;
        if (i != -1) {
            if (z2) {
                this.mUIImplementation.synchronouslyUpdateViewOnUIThread(i, this.mDiffMap);
            }
            if (z3) {
                this.mNodesManager.enqueueUpdateViewOnNativeThread(this.mConnectedViewTag, createMap2);
            }
            if (z4) {
                WritableMap createMap3 = Arguments.createMap();
                createMap3.putInt("viewTag", this.mConnectedViewTag);
                createMap3.putMap("props", createMap);
                this.mNodesManager.sendEvent("onReanimatedPropsChange", createMap3);
            }
        }
        return ZERO;
    }

    public void update() {
        if (this.mConnectedViewTag != -1) {
            value();
        }
    }
}
