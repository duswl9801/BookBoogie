package com.reading_app.bookboogie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SavedPassageListsAdapter extends RecyclerView.Adapter<SavedPassageListsAdapter.PassageImgViewHolder> {

    // 액티비티에서 전달 받을 비트맵 이미지 어레이리스트. 사용자가 형광펜 친 이미지들이 들어있음.
    ArrayList<Bitmap> save_imgs = new ArrayList<>();
    Context my_context;

    Bitmap save_img;

    String str;

    public class PassageImgViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView passage_imgview;

        public PassageImgViewHolder(@NonNull View itemView) {
            super(itemView);

            passage_imgview = itemView.findViewById(R.id.passage_img);

            // todo 길게 클릭했을때 크게보이도록 만들기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    str = getBase64String(save_imgs.get(getAdapterPosition()));

                    // 쉐어드에 저장하기
                    SharedPreferences prefs = my_context.getSharedPreferences("highlighted_img", MODE_PRIVATE);
                    // 작성한다고 선언.
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("str_highlighted_img", str);
                    editor.commit();

                    Intent intent = new Intent(my_context, PassageCheckActivity.class);
                    my_context.startActivity(intent);

                }
            });


            // OnCreateContextMenuListener를 현재 클래스에서 구현한다는 코드.
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }

    SavedPassageListsAdapter(Context context, ArrayList<Bitmap> save_imgs){
        my_context = context;
        this.save_imgs = save_imgs;
    }

    @NonNull
    @Override
    public PassageImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layout_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layout_inflater.inflate(R.layout.item_passage, parent, false);
        PassageImgViewHolder passageImgViewHolder = new PassageImgViewHolder(view);

        return passageImgViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PassageImgViewHolder holder, int position) {

        save_img = save_imgs.get(position);

        holder.passage_imgview.setImageBitmap(save_img);

    }

    @Override
    public int getItemCount() {
        return save_imgs.size();
    }

    // 비트맵 이미지를 문자열로 변환시켜주는 메소드.
    public String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }
}
