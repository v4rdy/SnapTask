package com.example.snaptask.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snaptask.EditNote;
import com.example.snaptask.helpers.Note;
import com.example.snaptask.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Note> noteList;
    private FirebaseDatabase firebasedatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    public NotesRecyclerViewAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NotesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.notes_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRecyclerViewAdapter.ViewHolder holder, final int position) {
        final Note note = noteList.get(position);
        holder.name.setText(note.getName());
        Log.e("TAG", "Name of note in adapter - "+note.getName());
        holder.date.setText(note.getDate());
        Log.e("TAG", "Date of note in adapter - "+note.getDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditNote.class);
                intent.putExtra("Title", noteList.get(position).getName());
                intent.putExtra("Text", noteList.get(position).getText());
                intent.putExtra("Date", noteList.get(position).getDate());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });

    }
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView name, date;
        private CardView cardView;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.note_name);
            date = (TextView) itemView.findViewById(R.id.note_date);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            cardView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(this.getAdapterPosition(), 1, 0, "Delete");

        }
    }

    public void deleteItem(int position){
        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebasedatabase.getReference("users").child(firebaseUser.getUid()).child("notes");
        databaseReference.child(noteList.get(position).getName()).removeValue();
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }
    }
