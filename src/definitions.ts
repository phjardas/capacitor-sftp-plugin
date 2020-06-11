declare module '@capacitor/core' {
  interface PluginRegistry {
    SFTP: SFTPPlugin;
  }
}

export type SFTPConnectOptions = {
  host: string;
  port?: number;
  username: string;
  password: string;
};

export type SFTPSessionOptions = {
  sessionId: string;
};

export type SFTPListFilesOptions = SFTPSessionOptions & {
  path: string;
};

export type SFTPCloseOptions = SFTPSessionOptions;

export type SFTPPlugin = {
  connect(options: SFTPConnectOptions): Promise<string>;
  listFiles(options: SFTPListFilesOptions): Promise<FileInfo[]>;
  close(options: SFTPCloseOptions): Promise<void>;
};

export type FileInfo = {
  path: string;
  name: string;
};

export class SFTPNotSupportedError extends Error {
  constructor() {
    super('SFTP is not supported on this platform');
    Object.setPrototypeOf(this, new.target.prototype);
    this.name = SFTPNotSupportedError.name;
  }
}
