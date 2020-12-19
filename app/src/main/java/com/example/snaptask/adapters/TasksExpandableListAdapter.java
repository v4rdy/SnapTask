package com.example.snaptask.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.core.content.ContextCompat;

import com.example.snaptask.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class TasksExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listHeader;
    private HashMap<String, List<String>> listChildren;
    private HashMap<String, String> priority;
    private HashMap<String, String> status;
    private String day;
    public TasksExpandableListAdapter(Context context, List<String> listHeader, HashMap<String, List<String>> listChildren, HashMap<String, String> priority, HashMap<String, String> status, String day){
        this.context = context;
        this.listHeader = listHeader;
        this.listChildren = listChildren;
        this.priority = priority;
        this.status = status;
        this.day = day;
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


    public Object getPriority(int groupPosition) {
        return priority.get(listHeader.get(groupPosition));
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
        String priorityImage = (String) getPriority(groupPosition);
        final String statusOfTask = (String) getStatus(groupPosition);

        Log.e("TAG", "Priority in adapter - " + priorityImage);
        Log.e("TAG", "Status in adapter - " + statusOfTask);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_header, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view_header);
        final TextView headerOfGroup = (TextView) convertView.findViewById(R.id.header_text);
        Log.e("TAG", "Task adapter :" + header);

        switch (priorityImage){
            case "c":
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.green));
                break;
            case "b":
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.yellow));
                break;
            case "a":
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.red));
                break;
        }
        headerOfGroup.setText(header);

        final CheckBox done = (CheckBox) convertView.findViewById(R.id.task_done_btn);

        if(statusOfTask.equals("true")){
            headerOfGroup.setTextColor(ContextCompat.getColor(context, R.color.hint2));
            done.setChecked(true);
        }else if(statusOfTask.equals("false")){
            headerOfGroup.setTextColor(ContextCompat.getColor(context, R.color.dark_blue));
            done.setChecked(false);
        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser;
                DatabaseReference dataBase;
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                dataBase = FirebaseDatabase.getInstance().getReference();
                    if (statusOfTask.equals("true")) {
                        headerOfGroup.setTextColor(ContextCompat.getColor(context, R.color.dark_blue));
                        dataBase.child("users").child(firebaseUser.getUid()).child("tasks").child(day).child(header).child("status").setValue("false");
                    } else if (statusOfTask.equals("false")) {
                        headerOfGroup.setTextColor(ContextCompat.getColor(context, R.color.hint2));
                        dataBase.child("users").child(firebaseUser.getUid()).child("tasks").child(day).child(header).child("status").setValue("true");
                    }
                TasksExpandableListAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }



    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        List<String> child = (List<String>) getChild(groupPosition, childPosition);
        String statusOfTask = (String) getStatus(groupPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_child, null);
        }

        TextView childText = (TextView) convertView.findViewById(R.id.child_text);
        Log.e("TAG", "Subtasks adapter :" + child.get(childPosition));
        childText.setText(child.get(childPosition));
        if(statusOfTask.equals("true")){
            childText.setTextColor(ContextCompat.getColor(context, R.color.hint2));
        }else if(statusOfTask.equals("false")){
           childText.setTextColor(ContextCompat.getColor(context, R.color.dark_blue));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
