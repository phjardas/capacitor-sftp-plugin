package de.jardas.capacitor.sftp;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NativePlugin()
public final class SFTPPlugin extends Plugin {
  private final Map<String, Session> sessions = new HashMap<>();

  @PluginMethod()
  public void connect(final PluginCall call) {
    try {
      final String host = call.getString("host");
      final int port = call.getInt("port", 22);
      final String username = call.getString("username");
      final String password = call.getString("password");

      final Session session = new Session(host, port, username, password);
      sessions.put(session.getId(), session);

      final JSObject ret = new JSObject();
      ret.put("sessionId", session.getId());
      call.success(ret);
    } catch (RuntimeException e) {
      call.error("Error connecting", e);
    }
  }

  @PluginMethod()
  public void listFiles(final PluginCall call) {
    try {
      final Session session = getSession(call);
      final List<FileInfo> files = session.listFiles(call.getString("path"));

      final JSObject ret = new JSObject();
      ret.put("sessionId", session.getId());
      final JSArray filesRet = new JSArray();
      for (FileInfo file : files) {
        filesRet.put(
          new JSObject()
            .put("path", file.getPath())
            .put("name", file.getName())
        );
      }
      ret.put("files", filesRet);
      call.success(ret);
    } catch (IOException | RuntimeException e) {
      call.error("Error listing files", e);
    }
  }

  @PluginMethod()
  public void close(final PluginCall call) {
    try {
      final Session session = getSession(call);
      session.close();
      call.success();
    } catch (RuntimeException e) {
      call.error("Error closing session", e);
    }
  }

  private Session getSession(PluginCall call) {
    final String sessionId = call.getString("sessionId");
    final Session session = sessions.get(sessionId);
    if (session == null)
      throw new IllegalArgumentException("SFTP session not found: " + sessionId);
    return session;
  }
}
