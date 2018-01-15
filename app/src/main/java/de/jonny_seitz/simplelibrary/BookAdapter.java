package de.jonny_seitz.simplelibrary;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jonny on 15/01/18.
 */

public class BookAdapter extends ArrayAdapter {

    public BookAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }
        Book book = (Book) getItem(position);
        ((TextView) convertView.findViewById(R.id.title)).setText(book.getTitle());
        ((TextView) convertView.findViewById(R.id.author)).setText(book.getAuthor());
        return convertView;
    }

}
