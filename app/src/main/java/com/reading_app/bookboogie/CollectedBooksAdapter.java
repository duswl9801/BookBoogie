package com.reading_app.bookboogie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CollectedBooksAdapter extends RecyclerView.Adapter<CollectedBooksAdapter.BookViewHolder> {

    // 어레이리스트를 쉐어드 프리퍼런스에 저장할 떄, key로 사용할 문자열 변수.
    // 쉐어드 프리퍼런스 불러올때 key로 사용할 문자열 변수.
    // 읽고 싶은 책
    private static final String WANT_READ_BOOKS = "want_read_books";
    // 읽은 책
    private static final String READ_BOOKS = "read_books";

    ArrayList<Book> books;
    Context my_context;
    Book book;
    int book_type = 0;
    String img_string;
    Bitmap img_bitmap;

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
                if( book_type == 0){
                    Log.d("book_url", books.get(getAdapterPosition()).getImage());
                    Log.d("book_url", "0");
                    Log.d("book_url", String.valueOf(books.get(getAdapterPosition()).isSearchedBook));

                    Intent intent = new Intent(my_context, ReadBookCheckActivity.class);
                    intent.putExtra("BookData", books.get(getAdapterPosition()).title);
                    my_context.startActivity(intent);
                }
                else if (book_type == 1){
                    Intent intent = new Intent(my_context, WantReadBookCheckActivity.class);
                    intent.putExtra("BookData", books.get(getAdapterPosition()).title);
                    my_context.startActivity(intent);
                }


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

                        Log.d("position_check", books.get(getAdapterPosition()).title);

                        delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                books.remove(getAdapterPosition());

                                // 쉐어드 프리퍼런스에서 먼저 삭제
                                if(book_type == 0){
//                                    // 쉐어드 프리퍼런스 열기.
//                                    SharedPreferences pref = my_context.getSharedPreferences("book_data", MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = pref.edit();
//                                    editor.remove(books.get(getAdapterPosition()).title);
//                                    editor.commit();

                                    SharedPreferences prefs = my_context.getSharedPreferences(READ_BOOKS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();

                                    Gson gson = new Gson();
                                    String json = gson.toJson(books);

                                    editor.putString("read_book", json);
                                    editor.commit();


                                } else if(book_type == 1){

//                                    SharedPreferences pref = my_context.getSharedPreferences("wanted_book_data", MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = pref.edit();
//                                    editor.remove(books.get(getAdapterPosition()).title);
//                                    editor.commit();
                                    ////////////////////////////////////
                                    // books 저장 시키기기

                                    SharedPreferences prefs = my_context.getSharedPreferences(WANT_READ_BOOKS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();

                                    Gson gson = new Gson();
                                    String json = gson.toJson(books);

                                    editor.putString("want_book", json);
                                    editor.commit();

                               }


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

    CollectedBooksAdapter(Context context, ArrayList<Book> books, int book_type){
        my_context = context;
        this.books = books;
        this.book_type = book_type;
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

        Log.d("uri_check", book.getImage().toString());


        if(book.isSearchedBook == true){     // 책의 이미지가 uri이 스트링으로 저장됨.

            Uri book_uri = Uri.parse(book.getImage());
            Glide.with(my_context).load(book_uri).into(holder.book_imgview);

        } else{       // 책의 이미지가 비트맵이 스트링으로 저장됨.

            img_string = book.getImage();

            // 문자열을 다시 비트맵으로 바꾸는 코드
            byte[] decodedByteArray = Base64.decode(img_string, Base64.NO_WRAP);
            img_bitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

            holder.book_imgview.setImageBitmap(img_bitmap);
        }

        holder.book_name_textview.setText(book.getTitle());
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public void changeItem(){
        notifyDataSetChanged();
    }






}
