package com.overdrive.app.camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumMap;
import java.util.Map;

/**
 * Fully resolved runtime camera configuration.
 */
public final class ResolvedCameraConfig {
    private final CameraProfile profile;
    private final String selectedProfileId;
    private final boolean autoProfile;
    private final int panoCameraId;
    private final int panoWidth;
    private final int panoHeight;
    private final int panoSurfaceMode;
    private final boolean manualPanoOverride;
    private final boolean validated;
    private final boolean fallbackFromProbe;
    private final EnumMap<CameraRole, CameraSourceRef> roleMappings;

    public ResolvedCameraConfig(
            CameraProfile profile,
            String selectedProfileId,
            boolean autoProfile,
            int panoCameraId,
            int panoWidth,
            int panoHeight,
            int panoSurfaceMode,
            boolean manualPanoOverride,
            boolean validated,
            boolean fallbackFromProbe,
            Map<CameraRole, CameraSourceRef> roleMappings) {
        this.profile = profile;
        this.selectedProfileId = selectedProfileId;
        this.autoProfile = autoProfile;
        this.panoCameraId = panoCameraId;
        this.panoWidth = panoWidth;
        this.panoHeight = panoHeight;
        this.panoSurfaceMode = panoSurfaceMode;
        this.manualPanoOverride = manualPanoOverride;
        this.validated = validated;
        this.fallbackFromProbe = fallbackFromProbe;
        this.roleMappings = new EnumMap<>(CameraRole.class);
        if (roleMappings != null) {
            this.roleMappings.putAll(roleMappings);
        }
    }

    public CameraProfile getProfile() {
        return profile;
    }

    public String getSelectedProfileId() {
        return selectedProfileId;
    }

    public boolean isAutoProfile() {
        return autoProfile;
    }

    public int getPanoCameraId() {
        return panoCameraId;
    }

    public int getPanoWidth() {
        return panoWidth;
    }

    public int getPanoHeight() {
        return panoHeight;
    }

    public int getPanoSurfaceMode() {
        return panoSurfaceMode;
    }

    public boolean isManualPanoOverride() {
        return manualPanoOverride;
    }

    public boolean isValidated() {
        return validated;
    }

    public boolean isFallbackFromProbe() {
        return fallbackFromProbe;
    }

    public EnumMap<CameraRole, CameraSourceRef> getRoleMappings() {
        return new EnumMap<>(roleMappings);
    }

    public JSONObject roleMappingsToJson() {
        JSONObject out = new JSONObject();
        for (Map.Entry<CameraRole, CameraSourceRef> entry : roleMappings.entrySet()) {
            putSafely(out, entry.getKey().getKey(), entry.getValue().toJson());
        }
        return out;
    }

    private static void putSafely(JSONObject object, String key, Object value) {
        try {
            object.put(key, value);
        } catch (JSONException e) {
            throw new IllegalStateException("Failed to write JSON field '" + key + "'", e);
        }
    }
}