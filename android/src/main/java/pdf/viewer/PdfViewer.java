package pdf.viewer;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleCoroutineScope;
import androidx.lifecycle.LifecycleKt;

import com.google.android.material.appbar.MaterialToolbar;
import com.rajat.pdfviewer.HeaderData;
import com.rajat.pdfviewer.PdfRendererView;
import com.rajat.pdfviewer.util.CacheStrategy;

public class PdfViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        /* ─── Edge-to-edge ─── */
        Window window = getWindow();
        WindowCompat.setDecorFitsSystemWindows(window, false);

        /* ─── Дані з плагіна ─── */
        String pdfUrl = getIntent().getStringExtra("pdf_url");
        String pdfTitle = getIntent().getStringExtra("pdf_title");
        String colorHex = getIntent().getStringExtra("toolbar_color");
        String colorTitle = getIntent().getStringExtra("toolbar_title_color");
        int toolbarColor = Color.parseColor(colorHex != null ? colorHex : "#A0BEFF");
        int titleColor = Color.parseColor(colorTitle != null ? colorTitle : "#000000");

        /* ─── Тулбар ─── */
        MaterialToolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(pdfTitle != null ? pdfTitle : "Документ");
        toolbar.setTitleTextColor(titleColor);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setBackgroundColor(toolbarColor);

        /* ─── Status-scrim зверху ─── */
        View statusScrim = findViewById(R.id.status_scrim);
        statusScrim.setBackgroundColor(toolbarColor);

//        /* ─── Insets: top-scrim + bottom-padding ─── */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root),
                (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    // висота scrim = висота статус-бара
                    statusScrim.getLayoutParams().height = bars.top;
                    statusScrim.requestLayout();

//                   // лише нижній паддінг, щоб PDF не зайшов під gesture-bar
                    v.setPadding(0, 0, 0, bars.bottom);
                    return insets;
                });

        /* ─── Колір status-bar (видно лише іконкам) ─── */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(toolbarColor);
        }

        /* ─── PDF ─── */
        PdfRendererView pdfView = findViewById(R.id.pdfView);
        LifecycleCoroutineScope scope = LifecycleKt.getCoroutineScope(getLifecycle());
        ProgressBar progress = findViewById(R.id.progressBar);

        /* 1. Підписуємо статус-лістенер */
        pdfView.setStatusListener(new PdfRendererView.StatusCallBack() {

            @Override
            public void onPdfLoadStart() {
                // показати індетермінований спінер
                progress.setIndeterminate(true);
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPdfLoadProgress(int percent, long downloaded, @Nullable Long total) {
                // якщо хочете смужку з %, зробіть ProgressBar горизонтальним у XML
                progress.setIndeterminate(false);
            }

            @Override
            public void onPdfRenderStart() {
                // тут нічого, можна змінити текст/статус
            }

            @Override
            public void onPdfRenderSuccess() {
                // PDF повністю відрендерено
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onPdfLoadSuccess(@NonNull String absPath) {
                // файл завантажено — рендер іще триває
            }

            @Override
            public void onPageChanged(int currentPage, int totalPage) {
                /* за бажанням: оновити UI */
            }

            @Override
            public void onError(@Nullable Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(
                        PdfViewer.this,
                        "Помилка PDF: " + (t != null ? t.getMessage() : "невідома"),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        progress.setVisibility(View.VISIBLE);
        pdfView.initWithUrl(
                pdfUrl,
                new HeaderData(),
                scope,
                getLifecycle(),
                CacheStrategy.MAXIMIZE_PERFORMANCE
        );
    }
}