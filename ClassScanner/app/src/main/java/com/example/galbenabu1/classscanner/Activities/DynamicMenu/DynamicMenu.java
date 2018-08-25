package com.example.galbenabu1.classscanner.Activities.DynamicMenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.galbenabu1.classscanner.R;

import java.util.ArrayList;
import java.util.List;

// TODO: not working yet, if theres no time, delete this class
// A singleton class. A dynamic menu that allows any view to initialize by adding MenuItems to it.
public class DynamicMenu {
    private static final String TAG = "DynamicMenu";
    private static final int MAX_NUMBER_OF_MENU_ITEMS = 4;
    private static final DynamicMenu ourInstance = new DynamicMenu();

    public static DynamicMenu getInstance() {
        return ourInstance;
    }

    private boolean mIsExpanded = false;
    private MenuItem mMainMenuItem;
    private List<MenuItem> mMenuItems;
    private ConstraintLayout mConstraintLayout;

    private DynamicMenu() {
        mMenuItems = new ArrayList<>();
    }

    public void setView(ConstraintLayout constraintLayout, Context context) {
        mConstraintLayout = constraintLayout;

        // Set main menu item
        mMainMenuItem = new MenuItem(context.getDrawable(R.drawable.common_google_signin_btn_icon_dark),
                context,
                () -> toggleExpand(true) // Will occur when main menu item is clicked.
             );

        // Set dynamic menu as a child view of constraintLayout.
        constraintLayout.addView(mMainMenuItem.mivItemImage, 0);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        mMainMenuItem.mivItemImage.setVisibility(View.VISIBLE);

        constraintSet.connect(mMainMenuItem.mivItemImage.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, -50);
        constraintSet.applyTo(constraintLayout);
    }

    public void addMenuItem(MenuItem menuItem, eMenuItemPosition menuItemPosition) {
        mMenuItems.add(menuItem);

        if(mMenuItems.size() > MAX_NUMBER_OF_MENU_ITEMS) {
            throw new IllegalStateException("Cannot have more than " + MAX_NUMBER_OF_MENU_ITEMS + " in the dynamic menu class");
        }


        // Set new menu item location compared to main menu item.
        int xDistanceFromMainMenuItem = 0;
        int yDistanceFromMainMenuItem = 0;

        switch (menuItemPosition) {
            case Top:
                // Upper menu item
                xDistanceFromMainMenuItem = 0;
                yDistanceFromMainMenuItem = 15;
                break;
            case Right:
                // Right menu item
                xDistanceFromMainMenuItem = 15;
                yDistanceFromMainMenuItem = 0;
                break;
            case Bottom:
                // Bottom menu item
                xDistanceFromMainMenuItem = 0;
                yDistanceFromMainMenuItem = -15;
                break;
            case Left:
                // Right menu item
                xDistanceFromMainMenuItem = -15;
                yDistanceFromMainMenuItem = 0;
                break;
        }

        menuItem.setLocationFromOtherMenuItem(mMainMenuItem, yDistanceFromMainMenuItem, xDistanceFromMainMenuItem); // Upper menu item
    }

    public MenuItem createMenuItem(Drawable drawable, Context context, Runnable action) {
        return new MenuItem(drawable, context, action);
    }

    private void toggleExpand(boolean shouldExpand) {
        Log.e(TAG, "Should expand menu: " + shouldExpand);
        for(MenuItem menuItem: mMenuItems) {
            menuItem.toggle(shouldExpand);
        }

        mIsExpanded = shouldExpand;
    }

    public class MenuItem {
        private Runnable mAction;
        private ImageView mivItemImage;

        public MenuItem(Drawable drawable, Context context, Runnable action) {
            mAction = action;
            mivItemImage = new ImageView(context);
            mivItemImage.setImageDrawable(drawable);
            mivItemImage.setMaxHeight(15);
            mivItemImage.setMaxWidth(15);

            mivItemImage.setOnClickListener(view -> {
                mAction.run();
                DynamicMenu.getInstance().toggleExpand(!mIsExpanded);
            });
        }

        private void toggle(boolean shouldShow) {
            mivItemImage.setVisibility(shouldShow ? View.VISIBLE : View.INVISIBLE);
        }

        private void setLocationFromOtherMenuItem(MenuItem otherMenuItem, float y, float x) {
            this.mivItemImage.setX(otherMenuItem.mivItemImage.getX() + x);
            this.mivItemImage.setY(otherMenuItem.mivItemImage.getY() + y);
        }
    }
}
