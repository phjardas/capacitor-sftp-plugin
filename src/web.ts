import { registerWebPlugin, WebPlugin } from '@capacitor/core';
import { FileInfo, SFTPNotSupportedError, SFTPPlugin } from './definitions';

export class SFTPWeb extends WebPlugin implements SFTPPlugin {
  constructor() {
    super({ name: 'SFTP', platforms: ['web'] });
  }

  async connect(): Promise<string> {
    throw new SFTPNotSupportedError();
  }

  async listFiles(): Promise<FileInfo[]> {
    throw new SFTPNotSupportedError();
  }

  async close(): Promise<void> {
    throw new SFTPNotSupportedError();
  }
}

export const SFTP = new SFTPWeb();
registerWebPlugin(SFTP);
