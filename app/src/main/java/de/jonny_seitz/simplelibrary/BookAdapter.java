package de.jonny_seitz.simplelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jonny on 15/01/18.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> books;

    public BookAdapter(Activity context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, null);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        Book book = books.get(position);
        holder.getTitle().setText(book.getTitle());
        holder.getAuthor().setText(book.getAuthor());
        holder.getListItem().setOnClickListener(new RecyclerView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("BOOK", books.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        private TextView title, author;
        private LinearLayout listItem;

        public BookViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            listItem = (LinearLayout) itemView.findViewById(R.id.list_item);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getAuthor() {
            return author;
        }

        public LinearLayout getListItem() {
            return listItem;
        }

    }

}
