package com.example.music_app1.View;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import java.io.File;

public class MusicDownloader {
    private Context context;

    public MusicDownloader(Context context) {
        this.context = context;
    }


    public void downloadMusic(String url, String fileName) {

        final File songFolder = new File(Environment.getExternalStorageDirectory(), "Music");

        // Kiểm tra xem thư mục Music đã tồn tại hay không
        if (!songFolder.exists()) {
            // Nếu thư mục không tồn tại, tạo mới thư mục với tên là Music
            if (songFolder.mkdirs()) {
                Log.d("FolderCreation", "Thư mục được tạo thành công");
            } else {
                Log.e("FolderCreation", "Không thể tạo thư mục");
            }
        }



        // Tạo tham chiếu đến Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url);

        // Tải tập tin từ Firebase Storage vào thư mục con
        final File file = new File(songFolder, fileName);
        storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Tải xuống thành công, hiển thị thông báo
                Toast.makeText(context, "Tải thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi có lỗi xảy ra trong quá trình tải xuống
                if (e instanceof StorageException) {
                    int errorCode = ((StorageException) e).getErrorCode();
                    Log.e("DownloadError", "HTTP result code: " + errorCode);
                }
                Log.e("DownloadError", e.getMessage());
                Toast.makeText(context, "Tải thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}