package com.agyer.windmill.test.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.agyer.windmill.core.miui.view.MiuiViewBlurUtil;
import com.agyer.windmill.test.R;


public class MiuiBlurActivity extends Activity {

    public MiuiBlurActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);

        TextView tv = findViewById(R.id.text);

        /*MiuiViewBlurUtil.setBackgroundBlur(frameLayout, 100);
        MiuiViewBlurUtil.setBlurMode(frameLayout, 0);*/

        if (MiuiViewBlurUtil.setBackgroundBlur(tv, 60, MiuiViewBlurUtil.BLUR_MODE_CONTENT_SHAPE_IGNORES_COLOR)) {

            /*MiuiViewBlurUtil.addBackgroundBlendColors(tv,
                    new int[]{0x60ffffff},
                    new int[]{MiuiViewBlurUtil.BLEND_MODE_MODULATE});*/

            Configuration configuration = getResources().getConfiguration();
            boolean nightModeActive = (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

            if (MiuiViewBlurUtil.addBackgroundBlendColor(tv, nightModeActive ? 0xBF000000 : Color.WHITE, MiuiViewBlurUtil.BLEND_MODE_COLOR_MIXING)) {

                String text = "MA\nKE\nINE";

                int heightDp = configuration.screenHeightDp;
                int widthDp = configuration.screenWidthDp;

                if (heightDp < widthDp) {
                    text = text.replace('\n', (char) 0);
                }

                tv.setText(text);

                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Math.min(heightDp, widthDp) / 2f);
                //部分模糊模式会忽略 view 本身的颜色
                tv.setTextColor(Color.RED);
                tv.getPaint().setFakeBoldText(true);
                return;
            }
        }

        tv.setText("系统不支持此功能。");
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }

}
