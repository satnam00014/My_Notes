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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.singh.mynotes.NoteListActivity;
import com.singh.mynotes.R;
import com.singh.mynotes.models.Folder;
import com.singh.mynotes.models.FolderWithNotes;
import com.singh.mynotes.viewmodel.FolderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<FolderWithNotes> folderWithNotes;
    private List<FolderWithNotes> totalFolderWithNotes;
    private FolderViewModel folderViewModel;
    public FolderRecyclerAdapter(Context context, List<FolderWithNotes> folderWithNotes) {
        this.context = context;
        //following is to sort using title name.
        Collections.sort(folderWithNotes,(o1, o2) -> o1.folder.getTitle().toLowerCase().compareTo(o2.folder.getTitle().toLowerCase()));
        this.folderWithNotes = folderWithNotes;
        this.totalFolderWithNotes = folderWithNotes;
        //Below line is to call folderViewModel class and is only used once to delete whole folder.
        folderViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(FolderViewModel.class);
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
               filterResults.count = totalFolderWithNotes.size();
               filterResults.values = totalFolderWithNotes;
               return filterResults;
            }
            List<FolderWithNotes> resultList = new ArrayList<>();
            String searchPattern = constraint.toString().toLowerCase().trim();
            for (FolderWithNotes folderWithNotes : totalFolderWithNotes) {
                if (folderWithNotes.folder.getTitle().toLowerCase().contains(searchPattern)) {
                    resultList.add(folderWithNotes);
                }
            }
            filterResults.count = resultList.size();
            filterResults.values = resultList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            folderWithNotes = (List<FolderWithNotes>) results.values;
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where where view in inflated and will return view holder with view(that means card)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_card, parent, false);
        return new ViewHolder((CardView) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //following is to set title of folder
        holder.folderName.setText(folderWithNotes.get(position).folder.getTitle());

        //following is to set number of notes in folder.
        holder.numberOfNotes.setText(folderWithNotes.get(position).notes.size() + " - Notes");

        //following is to set images
        Glide.with(context).load(R.drawable.folder_icon)
                .apply(RequestOptions.circleCropTransform()).thumbnail(0.3f).into(holder.folderImageView);
        holder.deleteButton.setOnClickListener(v -> {
            //call function to delete folder
            deleteFolderDialog(folderWithNotes.get(position).folder);
        });
        holder.currentCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteListActivity.class);
            intent.putExtra("folderId", folderWithNotes.get(position).folder.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return folderWithNotes.size();
    }

    private void deleteFolderDialog(Folder folder) {
        // create a dialog box from layout using layout inflater.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_delete_folder, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        //following is to disable dismiss if user touches outside the dialog box area
        alertDialog.setCanceledOnTouchOutside(false);
        //following is to add transparent background for round edges other wise white corner will be shown
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        view.findViewById(R.id.cancel_folder_delete_dialog_bt).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        view.findViewById(R.id.delete_folder_dialog_bt).setOnClickListener(v -> {
            folderViewModel.delete(folder);
            alertDialog.dismiss();
        });
    }

    // this is the view holder which holds the view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView currentCardView;
        private TextView folderName, numberOfNotes;
        private ImageView folderImageView;
        private FloatingActionButton deleteButton;
        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            this.currentCardView = cardView;
            this.folderName = cardView.findViewById(R.id.folder_name_card);
            this.numberOfNotes = cardView.findViewById(R.id.note_numbers_card);
            this.folderImageView = cardView.findViewById(R.id.folder_image_card);
            this.deleteButton = cardView.findViewById(R.id.delete_bt_folder_card);
        }
    }
}
