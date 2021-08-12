package com.colin.longpicture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * create by colin
 * 2021/7/23
 */
public class Callback extends ItemTouchHelper.Callback {


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        PictureAdapter adapter = (PictureAdapter) recyclerView.getAdapter();
        int fromPosition = viewHolder.getBindingAdapterPosition();
        int targetPosition = target.getBindingAdapterPosition();
        if (adapter != null) {
            adapter.notifyItemMoved(fromPosition, targetPosition);
            adapter.swap(fromPosition, targetPosition);
        }
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder != null)
            viewHolder.itemView.animate().scaleX(1.05f).scaleY(1.05f)
                    .start();
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.animate().scaleX(1f)
                .scaleY(1f).start();
    }
}
