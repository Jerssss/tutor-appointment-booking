package shared.classes;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, SessionData> activeSessions = new ConcurrentHashMap<>();
    private static final ThreadLocal<String> currentSessionToken = new ThreadLocal<>();

    private static class SessionData {
        private final String userId;
        private final String userRole;
        private final long creationTime;

        public SessionData(String userId, String userRole) {
            this.userId = userId;
            this.userRole = userRole;
            this.creationTime = System.currentTimeMillis();
        }
    }

    public static String createSession(String userId, String userRole) {
        String sessionToken = UUID.randomUUID().toString();
        activeSessions.put(sessionToken, new SessionData(userId, userRole));
        currentSessionToken.set(sessionToken);
        return sessionToken;
    }

    public static String getCurrentUserId() {
        String token = currentSessionToken.get();
        if (token == null) return null;

        SessionData data = activeSessions.get(token);
        return data != null ? data.userId : null;
    }

    public static String getCurrentUserRole() {
        String token = currentSessionToken.get();
        if (token == null) return null;

        SessionData data = activeSessions.get(token);
        return data != null ? data.userRole : null;
    }

    public static void endSession() {
        String token = currentSessionToken.get();
        if (token != null) {
            activeSessions.remove(token);
            currentSessionToken.remove();
        }
    }
}