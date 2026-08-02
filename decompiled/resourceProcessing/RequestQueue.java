/*
 * Decompiled with CFR 0.152.
 */
package resourceProcessing;

import java.util.ArrayList;
import java.util.List;
import resourceProcessing.ResourceRequest;

public class RequestQueue {
    private List<ResourceRequest> requestQueue = new ArrayList<ResourceRequest>();

    public synchronized void addRequest(ResourceRequest request) {
        this.requestQueue.add(request);
    }

    public synchronized ResourceRequest acceptNextRequest() {
        return this.requestQueue.remove(0);
    }

    public synchronized boolean hasRequests() {
        return !this.requestQueue.isEmpty();
    }
}

