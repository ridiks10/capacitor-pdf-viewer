package pdf.viewer;

import android.content.Intent;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "PdfViewer")
public class PdfViewerPlugin extends Plugin {

    @PluginMethod
    public void open(PluginCall call) {
        String url = call.getString("url");
        String title = call.getString("title", "Document");
        String toolbarColor = call.getString("toolbarColor");
        String toolbarTitleColor = call.getString("toolbarTitleColor", "#000000");

        if (url == null || url.isEmpty()) {
            call.reject("Missing URL");
            return;
        }

        try {
            Intent intent = new Intent(getContext(), PdfViewer.class);
            intent.putExtra("pdf_url", url);
            intent.putExtra("pdf_title", title);
            intent.putExtra("pdf_cache", "pdf.viewer.cache");
            intent.putExtra("toolbar_color", toolbarColor);
            intent.putExtra("toolbar_title_color", toolbarTitleColor);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getContext().startActivity(intent);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to launch viewer: " + e.getMessage());
        }
    }
}