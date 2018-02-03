package de.jonny_seitz.simplelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import io.realm.Realm;

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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        Book book = books.get(position);
        holder.getTitle().setText(book.getTitle());
        holder.getAuthor().setText(book.getAuthor());
        if (book.getCover() != null) {
            System.out.println(book.getCover().toString());
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    book.getCover()
            );
            if(file.exists()) {
                Bitmap bitmap = BitmapFactory
                        .decodeFile(file.getAbsolutePath(), new BitmapFactory.Options());
                int height = 150;
                bitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        bitmap.getWidth()/(bitmap.getHeight()/height),
                        height,
                        true
                );
                holder.getCover().setImageBitmap(bitmap);
            }
        }
        holder.getListItem().setOnClickListener(new RecyclerView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("BOOK-ID", books.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    public void removeBook(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {
        final int position = viewHolder.getAdapterPosition();
        final Book book = books.get(position);
        //Log.i("DB-Error", "removeBook\n"+book);
        Snackbar.make(recyclerView, R.string.book_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Log.i("DB-Error", "onClick\n"+book);
                                /**
                                 * not working correct: book (final) is deleted after declaration
                                 * check Log for DB-Error
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.copyToRealm(book);
                                realm.commitTransaction();
                                books.add(position, book);
                                notifyItemInserted(books.indexOf(position));
                                recyclerView.scrollToPosition(books.indexOf(position));
                                 **/
                            }
                        }
                ).show();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        book.deleteFromRealm();
        realm.commitTransaction();
        books.remove(position);
        this.notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        private TextView title, author;
        private ImageView cover;
        private LinearLayout listItem;

        public BookViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            listItem = (LinearLayout) itemView.findViewById(R.id.list_item);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getAuthor() {
            return author;
        }

        public ImageView getCover() {
            return cover;
        }

        public LinearLayout getListItem() {
            return listItem;
        }

    }

}
