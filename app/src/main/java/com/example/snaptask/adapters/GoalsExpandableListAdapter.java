package com.example.snaptask.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.snaptask.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class GoalsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listHeader;
    private HashMap<String, List<String>> listChildren;
    private HashMap<String, String> status;


    public GoalsExpandableListAdapter(Context context, List<String> listHeader, HashMap<String, List<String>> listChildren, HashMap<String, String> status){
        this.context = context;
        this.listHeader = listHeader;
        this.listChildren = listChildren;
        this.status = status;


    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChildren.get(listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }


    public Object getStatus(int groupPosition) {
        return status.get(listHeader.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChildren.get(listHeader.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String header = (String) getGroup(groupPosition);
        final String statusOfGoal = (String) getStatus(groupPosition);
        Log.e("TAG", "Status in adapter - " + statusOfGoal);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.goal_list_header, null);
        }
        final TextView headerOfGroup = (TextView) convertView.findViewById(R.id.header_text);
        Log.e("TAG", "Goal adapter :" + header);
        headerOfGroup.setText(header);

        final CheckBox done = (CheckBox) convertView.findViewById(R.id.goal_done_btn);
        final Button deleteGoal = (Button) convertView.findViewById(R.id.delete_goal);

        if(statusOfGoal.equals("true")){
            headerOfGroup.setTextColor(ContextCompat.getColor(context, R.color.hint2));
            deleteGoal.setVisibility(View.VISIBLE);
            done.setChecked(true);
        }else if(statusOfGoal.equals("false")){
            headerOfGroup.setTextColor(ContextCompat.getColor(context, R.color.dark_blue));
            deleteGoal.setVisibility(View.INVISIBLE);
            done.setChecked(false);
        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser;
                DatabaseReference dataBase;
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                dataBase = FirebaseDatabase.getInstance().getReference();
                    if (statusOfGoal.equals("true")) {
                        headerOfGroup.setTextColor(ContextCompat.getColor(context, R.color.dark_blue));
                        dataBase.child("users").child(firebaseUser.getUid()).child("goals").child(header).child("status").setValue("false");

                    } else if (statusOfGoal.equals("false")) {
                        headerOfGroup.setTextColor(ContextCompat.getColor(context, R.color.hint2));
                        dataBase.child("users").child(firebaseUser.getUid()).child("goals").child(header).child("status").setValue("true");

                    }
                GoalsExpandableListAdapter.this.notifyDataSetChanged();
            }
        });

        deleteGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser;
                DatabaseReference dataBase;
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                dataBase = FirebaseDatabase.getInstance().getReference();
                dataBase.child("users").child(firebaseUser.getUid()).child("goals").child(header).child("delete_status").setValue("true");
            }
        });
        return convertView;
    }



    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        List<String> child = (List<String>) getChild(groupPosition, childPosition);
        String statusOfGoal = (String) getStatus(groupPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.goal_list_child, null);
        }

        TextView childText = (TextView) convertView.findViewById(R.id.child_text);
        Log.e("TAG", "Steps adapter :" + child.get(childPosition));
        childText.setText(child.get(childPosition));
        if(statusOfGoal.equals("true")){
            childText.setTextColor(ContextCompat.getColor(context, R.color.hint2));
        }else if(statusOfGoal.equals("false")){
           childText.setTextColor(ContextCompat.getColor(context, R.color.dark_blue));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
