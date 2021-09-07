package com.singh.mynotes.RecyclerAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.singh.mynotes.EditNoteActivity;
import com.singh.mynotes.MoveNoteActivity;
import com.singh.mynotes.R;
import com.singh.mynotes.models.FolderWithNotes;
import com.singh.mynotes.models.Note;
import com.singh.mynotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Note> noteList;
    private List<Note> totalNoteList;
    private int folderId;
    private NoteViewModel noteViewModel;
    public NotesRecyclerAdapter(Context context,List<Note> noteList , int folderId) {
        this.context = context;
        this.folderId = folderId;
        Collections.sort(noteList,(o1, o2) -> o1.getNoteTitle().toLowerCase().compareTo(o2.getNoteTitle().toLowerCase()));
        this.noteList = noteList;
        this.totalNoteList = noteList;
        this.noteViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NoteViewModel.class);
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);
        return new ViewHolder((CardView) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.noteName.setText(noteList.get(position).getNoteTitle());
        holder.noteDate.setText(noteList.get(position).getNoteDate());
        Glide.with(context).load(R.drawable.note_icon)
                .apply(RequestOptions.circleCropTransform()).thumbnail(0.3f).into(holder.noteImageView);
        holder.moveButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, MoveNoteActivity.class);
            intent.putExtra("folderId",folderId);
            intent.putExtra("noteId",noteList.get(position).getId());
            context.startActivity(intent);
        });
        holder.deleteButton.setOnClickListener(v -> {
            Toast.makeText(context,"Delete clicked",Toast.LENGTH_SHORT).show();
            deleteNoteDialog(noteList.get(position));
        });
        holder.currentCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditNoteActivity.class);
            intent.putExtra("noteId",noteList.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                filterResults.count = totalNoteList.size();
                filterResults.values = totalNoteList;
                return filterResults;
            }
            List<Note> resultList = new ArrayList<>();
            String searchPattern = constraint.toString().toLowerCase().trim();
            for (Note note : totalNoteList) {
                if (note.getNoteTitle().toLowerCase().contains(searchPattern)) {
                    resultList.add(note);
                }
            }
            filterResults.count = resultList.size();
            filterResults.values = resultList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteList = (List<Note>) results.values;
            notifyDataSetChanged();
        }
    };
    // this is the view holder which holds the view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView currentCardView;
        private TextView noteName, noteDate;
        private ImageView noteImageView;
        private FloatingActionButton moveButton, deleteButton;
        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            this.currentCardView = cardView;
            this.noteName = cardView.findViewById(R.id.note_name_card);
            this.noteDate = cardView.findViewById(R.id.note_date_card);
            this.noteImageView = cardView.findViewById(R.id.note_image_card);
            this.moveButton = cardView.findViewById(R.id.edit_bt_note_card);
            this.deleteButton = cardView.findViewById(R.id.delete_bt_note_card);

        }
    }

    private void deleteNoteDialog(Note note){
        // create a dialog box from layout using layout inflater.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_delete_note, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        //following is to disable dismiss if user touches outside the dialog box area
        alertDialog.setCanceledOnTouchOutside(false);
        //following is to add transparent background for roundedges other wise white corner will be shown
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        view.findViewById(R.id.cancel_note_delete_dialog_bt).setOnClickListener(v -> {alertDialog.dismiss();});
        view.findViewById(R.id.delete_note_dialog_bt).setOnClickListener(v -> {
            noteViewModel.delete(note);
            alertDialog.dismiss();
        });
    }

    public boolean sortASC(){
        Collections.sort(noteList, (o1, o2) -> o1.getNoteTitle().toLowerCase().compareTo(o2.getNoteTitle().toLowerCase()));
        notifyDataSetChanged();
        return false;
    }
    public boolean sortDESC(){
        Collections.sort(noteList, (o2, o1) -> o1.getNoteTitle().toLowerCase().compareTo(o2.getNoteTitle().toLowerCase()));
        notifyDataSetChanged();
        return false;
    }
    public boolean sortDateDESC(){
        Collections.sort(noteList, new Comparator<Note>() {
            DateFormat f = new SimpleDateFormat("dd/M/yyyy hh:mm aa");
            @Override
            public int compare(Note o1, Note o2) {
                try {
                    return f.parse(o1.getNoteDate()).compareTo(f.parse(o2.getNoteDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException(e);
                }
            }
        });
        notifyDataSetChanged();
        return false;
    }
    public boolean sortDateASC(){
        Collections.sort(noteList, new Comparator<Note>() {
            DateFormat f = new SimpleDateFormat("dd/M/yyyy hh:mm aa");
            @Override
            public int compare(Note o2, Note o1) {
                try {
                    return f.parse(o1.getNoteDate()).compareTo(f.parse(o2.getNoteDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException(e);
                }
            }
        });
        notifyDataSetChanged();
        return false;
    }

}
