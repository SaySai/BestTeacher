package com.shanghai.haojiajiao.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shanghai.haojiajiao.R;

/**
 * Created by Administrator on 2015/12/30.
 */
public class ImgSwitchDialog extends Dialog {
    private Button choose_album, choose_cam, choose_cancel;
    private ImgSwitchLisenner lisenner;

    public ImgSwitchDialog(Context context, ImgSwitchLisenner lisenner) {
        super(context, R.style.Transparent);
        this.lisenner = lisenner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_avatar);
        choose_album = (Button) findViewById(R.id.choose_album);
        choose_cam = (Button) findViewById(R.id.choose_cam);
        choose_cancel = (Button) findViewById(R.id.choose_cancel);
        choose_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisenner.album();
                dismiss();
            }
        });
        choose_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisenner.cam();
                dismiss();
            }
        });
        choose_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface ImgSwitchLisenner {
        public void album();

        public void cam();
    }
}
