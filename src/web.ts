import { WebPlugin } from '@capacitor/core';
import type { PdfViewerPlugin } from './definitions';

export class PdfViewerWeb extends WebPlugin implements PdfViewerPlugin {
  async open(): Promise<void> {
    console.warn('PdfViewer is not supported on web.');
  }
}