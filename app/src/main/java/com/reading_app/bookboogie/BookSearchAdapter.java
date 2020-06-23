package com.reading_app.bookboogie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.ArrayList;

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.BookViewHolder> {

    ArrayList<Book> searched_books;
    Context my_context;


    public class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView book_cover;
        TextView book_title;
        TextView book_author;
        TextView book_price;
        TextView book_description;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            // 아이템 클릭이벤트 처리코드
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Todo 아이템 클릭하면 책 정보 바로 저장되도록 하기.
                    AlertDialog.Builder builder = new AlertDialog.Builder(my_context);
                    builder.setTitle("어디에 저장하시겠습니까?");
                    builder.setItems(R.array.book_state, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0) {
//                                Toast.makeText(my_context, "첫번째", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(my_context, AddReadBookActivity.class);
                                intent.putExtra("Book", searched_books.get(getAdapterPosition()));
                                my_context.startActivity(intent);
                            }
                            else if(which == 1) {
//                                Toast.makeText(my_context, "두번째", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(my_context, AddWantReadBookActivity.class);
                                intent.putExtra("Book", searched_books.get(getAdapterPosition()));
                                my_context.startActivity(intent);

                            }

                        }
                    });

                    AlertDialog choiced_book_dialog = builder.create();
                    choiced_book_dialog.show();


                }
            });

            book_cover = itemView.findViewById(R.id.bool_cover_img);
            book_title = itemView.findViewById(R.id.book_title);
            book_author = itemView.findViewById(R.id.book_author);
            book_price = itemView.findViewById(R.id.book_price);
            book_description = itemView.findViewById(R.id.book_description);

        }

    }

    BookSearchAdapter(Context context, ArrayList<Book> searched_books){
        my_context = context;
        this.searched_books = searched_books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layout_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layout_inflater.inflate(R.layout.item_searched_book, parent, false);
        BookSearchAdapter.BookViewHolder bookViewHolder = new BookSearchAdapter.BookViewHolder(view);

        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        Book book = searched_books.get(position);

//        holder.book_cover.setImageURI(book.getImage());
        Glide.with(my_context).load(book.getImage()).into(holder.book_cover);
        holder.book_title.setText(book.getTitle());
        holder.book_author.setText(book.getAuthor());
        holder.book_price.setText(book.getPrice());
        holder.book_description.setText(book.getDescription());

    }

    @Override
    public int getItemCount() {
        return searched_books.size();
    }

}
