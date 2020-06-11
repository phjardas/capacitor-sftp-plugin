package de.jardas.capacitor.sftp;

import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

final class Session implements Closeable {
  private static final String LOG_TAG = "capacitor-sftp-plugin";
  private final String id = UUID.randomUUID().toString();
  private com.jcraft.jsch.Session session;
  private ChannelSftp sftp;

  static {
    JSch.setConfig("StrictHostKeyChecking", "no");
  }

  public Session(String host, int port, String username, String password) {
    Log.i(LOG_TAG, "Connecting to " + username + "@" + host + ":" + port);

    try {
      final JSch jsch = new JSch();
      session = jsch.getSession(username, host, port);
      session.setPassword(password);
      session.connect();
      sftp = (ChannelSftp) session.openChannel("sftp");
      sftp.connect();
    } catch (JSchException e) {
      close();
      throw new RuntimeException("Error connecting SFTP: " + e, e);
    }
  }

  public List<FileInfo> listFiles(String path) throws IOException {
    try {
      Log.i(LOG_TAG, "Listing files at: " + path);
      final Vector<ChannelSftp.LsEntry> entries = sftp.ls(path);
      final List<FileInfo> files = new ArrayList<>(entries.size());
      for (final ChannelSftp.LsEntry entry : entries) {
        if (entry.getAttrs().isReg()) {
          files.add(new FileInfo(path, entry.getFilename()));
        }
      }
      return files;
    } catch (SftpException e) {
      switch (e.id) {
        case ChannelSftp.SSH_FX_NO_SUCH_FILE:
          throw new FileNotFoundException(path);
        default:
          throw new IOException("Error listing files at " + path + ": " + e, e);
      }
    }
  }

  public InputStream downloadFile(String path) throws IOException {
    try {
      Log.i(LOG_TAG, "Downloading file at: " + path);
      return sftp.get(path);
    } catch (SftpException e) {
      switch (e.id) {
        case ChannelSftp.SSH_FX_NO_SUCH_FILE:
          throw new FileNotFoundException(path);
        default:
          throw new IOException("Error downloading file at " + path + ": " + e, e);
      }
    }
  }

  public String getId() {
    return id;
  }

  @Override
  public void close() {
    Log.i(LOG_TAG, "Closing connection");
    if (sftp != null) {
      try {
        if (sftp.isConnected()) sftp.exit();
      } catch (RuntimeException e) {
        Log.w(LOG_TAG, "Error closing SFTP connection: " + e, e);
      }
    }

    try {
      if (session.isConnected()) session.disconnect();
    } catch (RuntimeException e) {
      Log.w(LOG_TAG, "Error closing SFTP connection: " + e, e);
    }
  }
}
