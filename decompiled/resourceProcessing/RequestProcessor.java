/*
 * Decompiled with CFR 0.152.
 */
package resourceProcessing;

import resourceProcessing.RequestQueue;
import resourceProcessing.ResourceRequest;

public class RequestProcessor
extends Thread {
    private static RequestProcessor PROCESSOR = new RequestProcessor();
    private RequestQueue requestQueue = new RequestQueue();
    private boolean running = true;

    public static void sendRequest(ResourceRequest request) {
        PROCESSOR.addRequestToQueue(request);
    }

    public static void cleanUp() {
        PROCESSOR.kill();
    }

    @Override
    public synchronized void run() {
        while (this.running || this.requestQueue.hasRequests()) {
            if (this.requestQueue.hasRequests()) {
                this.requestQueue.acceptNextRequest().doResourceRequest();
                continue;
            }
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void kill() {
        this.running = false;
        this.indicateNewRequests();
    }

    private synchronized void indicateNewRequests() {
        this.notify();
    }

    private RequestProcessor() {
        this.start();
    }

    private void addRequestToQueue(ResourceRequest request) {
        boolean isPaused = !this.requestQueue.hasRequests();
        this.requestQueue.addRequest(request);
        if (isPaused) {
            this.indicateNewRequests();
        }
    }
}

