import { WebPlugin } from '@capacitor/core';

import type { PdfViewerPlugin } from './definitions';

export class PdfViewerWeb extends WebPlugin implements PdfViewerPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
