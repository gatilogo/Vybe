package com.example.vybe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vybe.AddEdit.AddEditVibeEventActivity;
import com.example.vybe.Models.VibeEvent;

public class SwipeItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private MyVibesAdapter myVibesAdapter;
    private Context context;
    private String vibeEventDBPath;

    public SwipeItemTouchHelper(Context context, int dragDirs, int swipeDirs, MyVibesAdapter myVibesAdapter, String vibeEventDBPath) {
        super(dragDirs, swipeDirs);
        this.myVibesAdapter = myVibesAdapter;
        this.context = context;
        this.vibeEventDBPath = vibeEventDBPath;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT) {
            myVibesAdapter.deleteItem(position, vibeEventDBPath);
        } else if (direction == ItemTouchHelper.RIGHT) {
            Intent intent = new Intent(context, AddEditVibeEventActivity.class);
            VibeEvent vibeEvent = myVibesAdapter.getItem(position);
            intent.putExtra("vibeEvent", vibeEvent);
            context.startActivity(intent);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        drawSwipeAction(c, viewHolder, dX);
    }

    private void drawSwipeAction(Canvas c, RecyclerView.ViewHolder viewHolder, float dX) {
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        RectF swipeAction = new RectF();
        String msg = "";

        if (dX < 0) {   // Swipe left
            swipeAction.set(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            int deleteColor = ContextCompat.getColor(context, R.color.Delete);
            p.setColor(deleteColor);
            msg = "DELETE";
        } else if (dX > 0) {    // Swipe right
            swipeAction.set(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom());
            int position = viewHolder.getAdapterPosition();
            VibeEvent vibeEvent = myVibesAdapter.getItem(position);
            int vibeColor = vibeEvent.getVibe().getColor();
            int editColor = ContextCompat.getColor(context, vibeColor);
            p.setColor(editColor);
            msg = "EDIT";
        }

        c.drawRoundRect(swipeAction, corners, corners, p);
        drawText(msg, c, swipeAction, p);
    }

    private void drawText(String text, Canvas c, RectF swipeAction, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, swipeAction.centerX()-(textWidth/2), swipeAction.centerY()+(textSize/2), p);
    }
}
