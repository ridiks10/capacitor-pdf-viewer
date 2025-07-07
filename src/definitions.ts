export interface PdfViewerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
