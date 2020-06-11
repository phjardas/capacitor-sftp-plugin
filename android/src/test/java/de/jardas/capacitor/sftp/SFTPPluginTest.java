package de.jardas.capacitor.sftp;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * FIXME: these tests require a running SFTP server at localhost:2222
 */
public class SFTPPluginTest {
  @Test
  public void listFiles() throws Exception {
    try (final Session session = new Session("localhost", 2222, "test", "test")) {
      final List<FileInfo> files = session.listFiles("a");
      System.out.println("Files: " + files);
    }
  }

  @Test
  public void downloadFile() throws Exception {
    try (final Session session = new Session("localhost", 2222, "test", "test")) {
      final InputStream in = session.downloadFile("a/test.pdf");
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final byte[] buffer = new byte[4096];
      int len;
      while ((len = in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      System.out.println("Read " + out.size() + " bytes");
    }
  }
}
