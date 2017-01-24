package com.example.ivan.xmpppsbclient.userslist.view.holders;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;

/**
 * Created by I.Laukhin on 23.01.2017.
 */

public class RosterGroupViewHolder extends ParentViewHolder {

    private static final String TAG = "RosterGroupViewHolder";

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private final TextView userGroupName;
    private final ImageView arrowExpand;

    public RosterGroupViewHolder(@NonNull View itemView) {
        super(itemView);

        userGroupName = (TextView) itemView.findViewById(R.id.item_user_group_name);
        arrowExpand = (ImageView) itemView.findViewById(R.id.item_uset_group_arrow);
    }

    public void bind(@NonNull RosterGroupDecorator rosterGroup) {
        userGroupName.setText(rosterGroup.getName());
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                arrowExpand.setRotation(ROTATED_POSITION);
            } else {
                arrowExpand.setRotation(INITIAL_POSITION);
            }
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) {
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else {
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            arrowExpand.startAnimation(rotateAnimation);
        }
    }
}
