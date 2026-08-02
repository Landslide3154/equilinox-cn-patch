/*
 * Decompiled with CFR 0.152.
 */
package glRequestProcessing;

import glRequestProcessing.GlRequest;
import glRequestProcessing.GlRequestQueue;

public class GlRequestProcessor {
    private static final float MAX_TIME_MILLIS = 8.0f;
    private static GlRequestQueue requestQueue = new GlRequestQueue();

    public static void sendRequest(GlRequest request) {
        requestQueue.addRequest(request);
    }

    public static void dealWithTopRequests() {
        float remainingTime = 8000000.0f;
        long start = System.nanoTime();
        while (requestQueue.hasRequests()) {
            requestQueue.acceptNextRequest().executeGlRequest();
            long end = System.nanoTime();
            long timeTaken = end - start;
            start = end;
            if ((remainingTime -= (float)timeTaken) < 0.0f) break;
        }
    }

    public static void completeAllRequests() {
        while (requestQueue.hasRequests()) {
            requestQueue.acceptNextRequest().executeGlRequest();
        }
    }
}

