package com.example.music_app1.View;

import static android.view.animation.AnimationUtils.loadAnimation;
import static com.example.music_app1.adapter.MusicAdapter.mediaPlayer;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.music_app1.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentTransaction;




public class PlayMusic_Fragment extends Fragment {

    private boolean isExpanded = true; // Trạng thái mở rộng ban đầu
    private LinearLayout contentLayout;

    private ImageButton PlayPause;
    public static ImageView imgMusic;
    public static TextView nameMusic;
    public   static TextView nameArtist;

    public static SeekBar seekBar;
    public  static LinearLayout pageplaymusic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_music_, container, false);
        contentLayout = view.findViewById(R.id.content_layout);

        final LinearLayout layoutToCollapse = view.findViewById(R.id.pageplaymusic);
        ImageButton thugonButton = view.findViewById(R.id.thugon);
        thugonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) thugonButton.getLayoutParams();
                if (isExpanded) {
                    // Thu gọn ImageButton
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.START; // Đặt ở góc dưới bên trái
                    thugonButton.setLayoutParams(layoutParams);

                    // Mở rộng Fragment
                    contentLayout.setVisibility(View.VISIBLE);
                } else {
                    // Mở rộng ImageButton
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP; // Đặt lại vị trí giữa phía trên
                    thugonButton.setLayoutParams(layoutParams);

                    // Thu gọn Fragment
                    contentLayout.setVisibility(View.GONE);
                }

                isExpanded = !isExpanded; // Đảo ngược trạng thái
            }
        });


        imgMusic = view.findViewById(R.id.img_music);
        Animation rotation = AnimationUtils.loadAnimation(imgMusic.getContext(), R.anim.rotate);

//        // Lấy bitmap từ ImageView
//        Bitmap bitmap = ((BitmapDrawable) imgMusic.getDrawable()).getBitmap();
//        // Trích xuất các màu sắc nổi bật từ bitmap
//         extractProminentColors(bitmap);

        nameMusic = view.findViewById(R.id.name_music);

        nameArtist = view.findViewById(R.id.name_artist);

        seekBar = view.findViewById(R.id.seekbar_music);
        pageplaymusic = view.findViewById(R.id.pageplaymusic);
        PlayPause = view.findViewById(R.id.playpause);
        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    PlayPause.setImageResource(R.drawable.circle_play_regular);
                    Animation currentAnimation = imgMusic.getAnimation();

                }
                else{
                    mediaPlayer.start();
                    PlayPause.setImageResource(R.drawable.circle_pause_regular);
                    if (imgMusic.getAnimation() == null) {
                        // Lấy trạng thái góc quay từ tag của ImageView
                        Float savedRotation = (Float) imgMusic.getTag();
                        float rotation = savedRotation != null ? savedRotation : 0.0f;

                        // Khởi tạo animation quay với góc quay đã lưu
                        RotateAnimation rotationAnimation = new RotateAnimation(rotation, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotationAnimation.setDuration(20000);
                        rotationAnimation.setInterpolator(new LinearInterpolator());
                        rotationAnimation.setRepeatCount(Animation.INFINITE);

                        // Bắt đầu animation
                        imgMusic.startAnimation(rotationAnimation);
                    }
                }
            }
        });


        return view;
    }
//
//    private void extractProminentColors(Bitmap bitmap) {
//        // Sử dụng thư viện Palette để trích xuất các màu sắc nổi bật từ bitmap
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//                // Lấy màu sắc chủ đạo từ Palette
//                int dominantColor = palette.getDominantColor(ColorUtils.setAlphaComponent(0xFF000000, 255)); // Mặc định màu đen
//
//                // Lấy màu sắc thứ hai từ Palette
//                Palette.Swatch swatch = palette.getLightVibrantSwatch();
//                int lightVibrantColor = swatch != null ? swatch.getRgb() : 0xFFFFFFFF; // Mặc định màu trắng
//
//                // Tạo GradientDrawable với các màu sắc đã trích xuất
//                GradientDrawable gradientDrawable = new GradientDrawable(
//                        GradientDrawable.Orientation.TOP_BOTTOM,
//                        new int[]{dominantColor, lightVibrantColor}
//                );
//
//                // Đặt GradientDrawable làm nền cho fragment
//                getView().setBackground(gradientDrawable);
//            }
//        });
//    }


}