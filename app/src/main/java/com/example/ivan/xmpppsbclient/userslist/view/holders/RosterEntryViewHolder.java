package com.example.ivan.xmpppsbclient.userslist.view.holders;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.enrities.RosterEntryDecorator;
import com.example.ivan.xmpppsbclient.userslist.view.RecyclerItemClickListener;

import org.jivesoftware.smack.packet.Presence;


public class RosterEntryViewHolder extends ChildViewHolder implements View.OnClickListener {

    private static final String TAG = "RosterEntryViewHolder";

    private RecyclerItemClickListener recyclerItemClickListener;
    private String userJid;
    private String userName;

    private TextView cardUserImage;
    private TextView cardUserPresence;
    private TextView cardUsesrName;
    private TextView cardUserStatus;

    public RosterEntryViewHolder(@NonNull View itemView) {
        super(itemView);

        Log.d(TAG, "ChildHolder");

        cardUserImage = (TextView) itemView.findViewById(R.id.card_tv_image);
        cardUserPresence = (TextView) itemView.findViewById(R.id.card_presence);
        cardUsesrName = (TextView) itemView.findViewById(R.id.card_username);
        cardUserStatus = (TextView) itemView.findViewById(R.id.card_status);

        itemView.setOnClickListener(this);
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public void bind(@NonNull RosterEntryDecorator rosterEntry, Presence presence) {

        userJid = rosterEntry.getUser();
        userName = rosterEntry.getName();

        if (presence != null) {
            cardUserImage.setText(String.valueOf(rosterEntry.getName().charAt(0)).toUpperCase());
            cardUsesrName.setText(userName);
            cardUserStatus.setText(presence.getStatus());
            if (!presence.isAvailable()) {
                cardUserPresence.setBackgroundResource(R.drawable.circle_textview_offline);
            } else {
                cardUserPresence.setBackgroundResource(R.drawable.circle_textview_online);
            }
        } else {
            cardUserImage.setText(String.valueOf(rosterEntry.getName().charAt(0)).toUpperCase());
            cardUsesrName.setText(userName);
            cardUserPresence.setBackgroundResource(R.drawable.circle_textview_offline);
        }
    }


    @Override
    public void onClick(View view) {
        if (recyclerItemClickListener != null) {
            Log.d(TAG, "onClick");
            recyclerItemClickListener.onItemClickListener(userJid, userName);
        }
    }
}
