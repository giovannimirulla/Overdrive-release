package com.overdrive.app.camera;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Serializable pointer to a direct camera or a panoramic subview.
 */
public final class CameraSourceRef {
    private final CameraSourceKind kind;
    private final Integer cameraId;
    private final CameraVirtualView virtualView;

    private CameraSourceRef(CameraSourceKind kind, Integer cameraId, CameraVirtualView virtualView) {
        this.kind = kind;
        this.cameraId = cameraId;
        this.virtualView = virtualView;
    }

    public static CameraSourceRef direct(int cameraId) {
        return new CameraSourceRef(CameraSourceKind.DIRECT, cameraId, null);
    }

    public static CameraSourceRef panoramic(CameraVirtualView virtualView) {
        return new CameraSourceRef(CameraSourceKind.PANORAMIC_VIRTUAL, null, virtualView);
    }

    public CameraSourceKind getKind() {
        return kind;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public CameraVirtualView getVirtualView() {
        return virtualView;
    }

    public String getStableId() {
        if (kind == CameraSourceKind.DIRECT) {
            return "direct:" + cameraId;
        }
        return "pano:" + (virtualView != null ? virtualView.getId() : "unknown");
    }

    public String getDisplayLabel() {
        if (kind == CameraSourceKind.DIRECT) {
            return "Direct camera " + cameraId;
        }
        return virtualView != null ? virtualView.getDisplayName() : "Panoramic view";
    }

    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        putSafely(out, "kind", kind.getId());
        if (cameraId != null) {
            putSafely(out, "cameraId", cameraId.intValue());
        }
        if (virtualView != null) {
            putSafely(out, "view", virtualView.getId());
        }
        putSafely(out, "id", getStableId());
        putSafely(out, "label", getDisplayLabel());
        return out;
    }

    private static void putSafely(JSONObject object, String key, Object value) {
        try {
            object.put(key, value);
        } catch (JSONException e) {
            throw new IllegalStateException("Failed to write JSON field '" + key + "'", e);
        }
    }

    public static CameraSourceRef fromJson(JSONObject obj) {
        if (obj == null) return null;
        CameraSourceKind kind = CameraSourceKind.fromId(obj.optString("kind", null));
        if (kind == CameraSourceKind.DIRECT && obj.has("cameraId")) {
            return direct(obj.optInt("cameraId", -1));
        }
        if (kind == CameraSourceKind.PANORAMIC_VIRTUAL) {
            CameraVirtualView view = CameraVirtualView.fromId(obj.optString("view", null));
            if (view != null) {
                return panoramic(view);
            }
        }
        return null;
    }
}