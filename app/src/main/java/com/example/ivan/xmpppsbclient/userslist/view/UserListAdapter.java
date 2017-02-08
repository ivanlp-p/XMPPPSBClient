package com.example.ivan.xmpppsbclient.userslist.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.common.XMPPPSBApplication;
import com.example.ivan.xmpppsbclient.enrities.RosterEntryDecorator;
import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;
import com.example.ivan.xmpppsbclient.userslist.view.holders.RosterEntryViewHolder;
import com.example.ivan.xmpppsbclient.userslist.view.holders.RosterGroupViewHolder;
import com.example.ivan.xmpppsbclient.xmpp.OpenfireConnection;

import org.jivesoftware.smack.packet.Presence;

import java.util.List;

import javax.inject.Inject;


public class UserListAdapter extends ExpandableRecyclerAdapter<RosterGroupDecorator, RosterEntryDecorator, RosterGroupViewHolder, RosterEntryViewHolder>{

    private static final String TAG = "UserListAdapter";

    @Inject
    OpenfireConnection connection;

    private List<RosterGroupDecorator> rosterGroups;
    private LayoutInflater inflater;
    private RecyclerItemClickListener recyclerItemClickListener;

    public UserListAdapter(Context context, List<RosterGroupDecorator> rosterGroups) {
        super(rosterGroups);
        this.rosterGroups = rosterGroups;
        inflater = LayoutInflater.from(context);
        XMPPPSBApplication.component().inject(this);
    }

    void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @NonNull
    @Override
    public RosterGroupViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        Log.d(TAG, "onCreateParentViewHolder");

        View view = inflater.inflate(R.layout.user_group_item, parentViewGroup, false);

        RosterGroupViewHolder holder = new RosterGroupViewHolder(view);
        return holder;
    }

    @NonNull
    @Override
    public RosterEntryViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        Log.d(TAG, "onCreateChildViewHolder");

        View view = inflater.inflate(R.layout.card_user, childViewGroup, false);

        RosterEntryViewHolder holder = new RosterEntryViewHolder(view);
        holder.setRecyclerItemClickListener(recyclerItemClickListener);
        return holder;
    }

    @Override
    public void onBindParentViewHolder(@NonNull RosterGroupViewHolder rosterGroupViewHolder, int parentPosition, @NonNull RosterGroupDecorator parent) {
        Log.d(TAG, "onBindParentViewHolder");
        rosterGroupViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull RosterEntryViewHolder rosterEntryViewHolder, int parentPosition, int childPosition, @NonNull RosterEntryDecorator child) {
        Log.d(TAG, "onBindChildViewHolder");
        if (connection.getUserPresence(child.getUserJid()) != null) {
            Presence presence = connection.getUserPresence(child.getUserJid());
            Log.d(TAG, "isAvailable " + presence.isAvailable());
            rosterEntryViewHolder.bind(child, presence);
        } else {
            rosterEntryViewHolder.bind(child);
        }


    }
}
