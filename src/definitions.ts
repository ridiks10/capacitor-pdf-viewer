export interface PdfViewerPlugin {
  open(options: { url: string; title?: string, toolbarColor?: string, toolbarTitleColor?: string }): Promise<void>;
}