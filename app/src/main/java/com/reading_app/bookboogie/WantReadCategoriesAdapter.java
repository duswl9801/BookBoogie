package com.reading_app.bookboogie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WantReadCategoriesAdapter extends RecyclerView.Adapter<WantReadCategoriesAdapter.CategoryViewHolder> {

    // 책 카테고리가 들어갈 어레이리스트. 기본적인것은 strings.xml에 선언되어 있고 사용자가 추가 수정 삭제 가능.
    ArrayList<String> catrgories;
    Context my_context;

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView category_textview;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            // 아이템 클릭이벤트 처리코드
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(my_context, CollectedBooksActivity.class);
                    intent.putExtra("category", catrgories.get(getAdapterPosition()));
                    my_context.startActivity(intent);

                    // ---------------------------여기서 데이터도 전달해 주어야함. 필요한 데이터: 카테고리 이름과 책들 어레이리스트

                }
            });

            category_textview = itemView.findViewById(R.id.category);

            // OnCreateContextMenuListener를 현재 클래스에서 구현한다는 코드.
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem category_edit = menu.add(Menu.NONE, 0, Menu.NONE, "수정하기");
            MenuItem category_delete = menu.add(Menu.NONE, 1, Menu.NONE, "삭제하기");
            category_edit.setOnMenuItemClickListener(onEditMenu);
            category_delete.setOnMenuItemClickListener(onEditMenu);
        }

        MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 0: // 수정하기 항목 선택

                        AlertDialog.Builder builder = new AlertDialog.Builder(my_context);
                        View view = LayoutInflater.from(my_context).inflate(R.layout.dialog_edit_category, null, false);
                        builder.setView(view);

                        final EditText edited_category_name = (EditText)view.findViewById(R.id.editedCategoryName);
                        final Button category_edit_btn = (Button)view.findViewById(R.id.category_edit_btn);
                        final Button category_cancle_btn = (Button)view.findViewById(R.id.category_cancle_btn);

                        // 해당 아이템에 입력되어 있던 데이터를 불러와서 다이얼로그에 보여줌
                        edited_category_name.setText(catrgories.get(getAdapterPosition()));

                        final AlertDialog category_edit_dialog = builder.create();

                        category_edit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String category_name = edited_category_name.getText().toString();

                                catrgories.set(getAdapterPosition(), category_name);

                                notifyItemChanged(getAdapterPosition());

                                category_edit_dialog.dismiss();

                            }
                        });

                        category_cancle_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                category_edit_dialog.cancel();
                            }
                        });

                        category_edit_dialog.show();

                        break;

                    case 1: // 삭제하기 항목 선택
                        androidx.appcompat.app.AlertDialog.Builder dialog_builder = new androidx.appcompat.app.AlertDialog.Builder(my_context);
                        View dialog_view = LayoutInflater.from(my_context).inflate(R.layout.dialog_check_delete, null, false);
                        dialog_builder.setView(dialog_view);

                        final Button delete_btn = (Button)dialog_view.findViewById(R.id.delete_btn);
                        final Button cancle_btn = (Button)dialog_view.findViewById(R.id.cancle_btn);

                        final androidx.appcompat.app.AlertDialog check_delete_dialog = dialog_builder.create();

                        delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                catrgories.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), catrgories.size());

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

//    WantReadCategoriesAdapter(ArrayList<String> catrgories){this.catrgories = catrgories;}
    WantReadCategoriesAdapter(Context context, ArrayList<String> categories){
        my_context = context;
        this.catrgories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layout_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layout_inflater.inflate(R.layout.item_category, parent, false);
        WantReadCategoriesAdapter.CategoryViewHolder categoryViewHolder = new WantReadCategoriesAdapter.CategoryViewHolder(view);

        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = catrgories.get(position);

        holder.category_textview.setText(category);
    }

    @Override
    public int getItemCount() {
        return catrgories.size();
    }



}
