package de.jardas.capacitor.sftp;

final class FileInfo {
  private final String path;
  private final String name;

  FileInfo(String path, String name) {
    this.path = path;
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "FileInfo{" +
      "path='" + path + '\'' +
      ", name='" + name + '\'' +
      '}';
  }
}
