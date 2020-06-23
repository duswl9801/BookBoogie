package com.reading_app.bookboogie;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.ArrayList;

public class CollectedBooksAdapter extends RecyclerView.Adapter<CollectedBooksAdapter.BookViewHolder> {

    ArrayList<Book> books;
    Context my_context;
    Book book;

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        ImageView book_imgview;
        TextView book_name_textview;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            book_imgview = itemView.findViewById(R.id.book_img);
            book_name_textview = itemView.findViewById(R.id.book_name_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(my_context, book.getImage().toString(), Toast.LENGTH_SHORT);
                    Toast.makeText(my_context, book.getMemo(), Toast.LENGTH_SHORT);
                    Toast.makeText(my_context, String.valueOf(book.getRating()), Toast.LENGTH_SHORT);


                }
            });

            // OnCreateContextMenuListener를 현재 클래스에서 구현한다는 코드.
            itemView.setOnCreateContextMenuListener(this);


        }

        // 컨텍스트 메뉴를 생성하고 메뉴 선택시 호출되는 리스너를 등록해줌. id 0, 1로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됨.
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            MenuItem book_delete = menu.add(Menu.NONE, 0, Menu.NONE, "삭제하기");
            book_delete.setOnMenuItemClickListener(onEditMenu);
        }

        MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0: // 삭제하기 항목 선택

                        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(my_context);
                        View dialog_view = LayoutInflater.from(my_context).inflate(R.layout.dialog_check_delete, null, false);
                        dialog_builder.setView(dialog_view);

                        final Button delete_btn = (Button)dialog_view.findViewById(R.id.delete_btn);
                        final Button cancle_btn = (Button)dialog_view.findViewById(R.id.cancle_btn);

                        final AlertDialog check_delete_dialog = dialog_builder.create();

                        delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                books.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), books.size());

                                check_delete_dialog.dismiss();
                            }
                        });

                        cancle_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                check_delete_dialog.cancel();
                            }
                        });

                        check_delete_dialog.show();

                        break;
                }
                return true;
            }
        };

    }

    CollectedBooksAdapter(Context context, ArrayList<Book> books){
        my_context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layout_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layout_inflater.inflate(R.layout.item_book, parent, false);
        BookViewHolder bookViewHolder = new BookViewHolder(view);

        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

//        Book book = books.get(position);
        book = books.get(position);

        Uri book_uri = book.getImage();

        Log.d("uri_check", book.getImage().toString());

        if(book_uri.toString().equals("image")){// 이미지 없을 경우
            holder.book_imgview.setImageResource(R.drawable.book_cover_sample);
        }else {
            //todo 글라이드 쓰는거로 바꿔야함.
//            holder.book_imgview.setImageURI(book_uri);
            Glide.with(my_context).load(book_uri).into(holder.book_imgview);
        }

        holder.book_name_textview.setText(book.getTitle());
    }


    @Override
    public int getItemCount() {
        return books.size();
    }
}
