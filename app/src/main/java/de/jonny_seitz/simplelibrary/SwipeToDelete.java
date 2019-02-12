package de.jonny_seitz.simplelibrary;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Jonny on 23/01/18.
 */

public class SwipeToDelete extends ItemTouchHelper.SimpleCallback {

    private BookAdapter adapter;
    private RecyclerView recyclerView;

    public SwipeToDelete(BookAdapter adapter, RecyclerView recyclerView) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter = adapter;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.removeBook(viewHolder, recyclerView);
    }

    @Override
    public boolean onMove(
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {
        return false;
    }

}
