package com.reading_app.bookboogie;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SavedPassageListsAdapter extends RecyclerView.Adapter<SavedPassageListsAdapter.PassageImgViewHolder> {

    // 액티비티에서 전달 받을 비트맵 이미지 어레이리스트. 사용자가 형광펜 친 이미지들이 들어있음.
    ArrayList<Bitmap> save_imgs = new ArrayList<>();
    Context my_context;

    Bitmap save_img;

    public class PassageImgViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView passage_imgview;

        public PassageImgViewHolder(@NonNull View itemView) {
            super(itemView);

            passage_imgview = itemView.findViewById(R.id.passage_img);

            // todo 길게 클릭했을때 크게보이도록 만들기

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
}
