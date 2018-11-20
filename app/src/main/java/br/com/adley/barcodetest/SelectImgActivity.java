package br.com.adley.barcodetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class SelectImgActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 2;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_img_acitivty);
        img = findViewById(R.id.img_result);
        findViewById(R.id.fab).setOnClickListener(view ->    {
            final String[] ACCEPT_MIME_TYPES = {
                    //"application/pdf",
                    "image/*"
            };
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        findViewById(R.id.btn_process).setOnClickListener(view -> {
            // Carrega a imagem
            Bitmap myBitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();

            // Verifica se o leitor de barras e Google play services estão disponível
            BarcodeDetector detector =
                    new BarcodeDetector.Builder(getApplicationContext())
                            .setBarcodeFormats(Barcode.ALL_FORMATS)
                            .build();
            if (!detector.isOperational()) {
                LinearLayout linearLayout = findViewById(R.id.content_layout);
                Snackbar snackbar = Snackbar.make(linearLayout, "Could not set up the detector!", Snackbar.LENGTH_LONG);
                snackbar.show();
                return;
            }

            // Realiza a leitura do código de barras
            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);

            // Itera sobre os resultados
            Barcode thisCode = barcodes.valueAt(0);
            TextView txtView = findViewById(R.id.txt_view_result);
            txtView.setText(thisCode.rawValue);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                Uri imageUri = resultData.getData();
                img.setImageURI(imageUri);
                findViewById(R.id.layout_result).setVisibility(View.VISIBLE);
            }
        }
    }

}
