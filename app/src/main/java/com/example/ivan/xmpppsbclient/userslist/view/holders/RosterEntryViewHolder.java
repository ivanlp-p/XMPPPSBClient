package com.example.ivan.xmpppsbclient.userslist.view.holders;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.userslist.view.RecyclerItemClickListener;

import org.jivesoftware.smack.roster.RosterEntry;

/**
 * Created by I.Laukhin on 23.01.2017.
 */

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

    public void bind(@NonNull RosterEntry rosterEntry) {

        userJid = rosterEntry.getUser();
        userName = rosterEntry.getName();

        cardUserImage.setText(String.valueOf(rosterEntry.getName().charAt(0)).toUpperCase());
        cardUsesrName.setText(userName);
//        cardUserStatus.setText(rosterEntry.getStatus().toString());
    }


    @Override
    public void onClick(View view) {
        if (recyclerItemClickListener != null) {
            Log.d(TAG, "onClick");
            recyclerItemClickListener.onItemClickListener(userJid, userName);
        }
    }
}
