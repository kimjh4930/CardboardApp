package com.test.cardboardtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import java.io.File;

public class MainActivity extends CardboardActivity {

    final String TAG = "ViewerActivity";

    private static final int REQUEST_PERMISSIONS = 10;

    private CardboardView.StereoRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한이 없는 경우
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            //최초 거부를 선택하면 두번째부터 이벤트 발생 & 권한 획득이 필요한 이유유를 설명
           if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }

            //요청 팝업 팝업 선택시 onRequestPermissionsResult 이동
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

        }
        //권한이 있는 경우
        else {
            String address = "/storage/emulated/0/DCIM/friendsCameraSample/20160820_173510.mp4";
            Uri uri = Uri.parse(address);
            String type = "video";

            if (type.contains("image")) {
                type = "image";
                //mRenderer = new ImageRenderer(this, uri);
            } else if (type.contains("video")) {
                type = "video";
                mRenderer = new VideoRenderer(this, uri);
            } else {
                finish();
            }

            CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
            cardboardView.setSettingsButtonEnabled(false);
            cardboardView.setRestoreGLStateEnabled(false);
            cardboardView.setRenderer(mRenderer);
            setCardboardView(cardboardView);
            getCardboardView().setVRModeEnabled(false);
        }


    }

    protected void onPause() {
        super.onPause();
        if (mRenderer instanceof VideoRenderer)
            ((VideoRenderer) mRenderer).pauseMediaPlayer();
    }
}
