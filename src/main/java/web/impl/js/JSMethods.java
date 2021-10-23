package web.impl.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.websocket.WebSocket;

public final class JSMethods {

    private JSMethods() {

    }

    @JSBody(params = {"obj", "propertyName"}, script = "return typeof obj[propertyName] != 'undefined'")
    public static native boolean has(JSObject obj, String propertyName);

    @JSBody(params = {"ws", "data"}, script = "return ws.send(data);")
    public static native void send(WebSocket ws, Uint8Array data);

    @JSBody(params = {"arr", "type"}, script = "return new Blob([arr], {type:type});")
    public static native JSObject blobify(Uint8Array arr, String type);

    @JSBody(params = {"blob"}, script = "return (window.URL || window.webkitURL).createObjectURL(blob);")
    public static native String createObjectUrl(JSObject blob);

    @JSBody(params = {"url"}, script = "return (window.URL || window.webkitURL).revokeObjectURL(url);")
    public static native void revokeObjectURL(String url);

    @JSBody(params = { "name", "value"}, script = "return window[name] = value")
    public static native void export(String name, JSObject value);
}
