package com.singh.mynotes.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.singh.mynotes.MoveNoteActivity;
import com.singh.mynotes.R;
import com.singh.mynotes.models.Folder;
import com.singh.mynotes.models.Note;
import com.singh.mynotes.viewmodel.NoteViewModel;

import java.util.List;

public class MoveNoteRecyclerAdapter extends RecyclerView.Adapter<MoveNoteRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Folder> folderList;
    private NoteViewModel noteViewModel;
    private Note note;

    public MoveNoteRecyclerAdapter(Context context, List<Folder> folderList,int noteId) {
        this.context = context;
        this.folderList = folderList;
        noteViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NoteViewModel.class);
        this.note = noteViewModel.getNote(noteId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where where view in inflated and will return view holder with view(that means card)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.move_note_card, parent, false);
        return new ViewHolder((CardView) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //instance of card on which we are performing operations.
        holder.folderName.setText(folderList.get(position).getTitle());
        Glide.with(context).load(R.drawable.folder_icon)
                .apply(RequestOptions.circleCropTransform()).thumbnail(0.3f).into(holder.folderImageView);
        holder.currentCardView.setOnClickListener(v -> {
            //Logic to move note.
            note.setFolderId(folderList.get(position).getId());
            noteViewModel.update(note);
            ((MoveNoteActivity)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    // this is the view holder which holds the view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView currentCardView;
        private TextView folderName;
        private ImageView folderImageView;

        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            this.currentCardView = cardView;
            this.folderName = cardView.findViewById(R.id.folder_name_move_card);
            this.folderImageView = cardView.findViewById(R.id.folder_image_move_card);
        }
    }

}
