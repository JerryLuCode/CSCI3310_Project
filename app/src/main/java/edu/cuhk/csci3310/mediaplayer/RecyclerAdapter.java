package edu.cuhk.csci3310.mediaplayer;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

public class RecyclerAdapter extends Adapter<RecyclerAdapter.BuildingViewHolder>  {
    private Context context;
    private LayoutInflater mInflater;
    private LinkedList<String> imagePathList = new LinkedList<>();
    private LinkedList<String> mediaTitleList = new LinkedList<>();
    private LinkedList<String> mediaLengthList = new LinkedList<>();
    View root;
    class BuildingViewHolder extends RecyclerView.ViewHolder {

        ImageView mediaImage;
        TextView mediaTitle;
        TextView mediaLength;

        View itemView;

        final RecyclerAdapter mAdapter;

        LinearLayout layoutItemView;


        public BuildingViewHolder(View itemView, RecyclerAdapter adapter) {
            super(itemView);
            this.itemView = itemView;

            layoutItemView = itemView.findViewById(R.id.media_item);

            this.mAdapter = adapter;

            layoutItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the item that was clicked.
                    int position = getLayoutPosition();

                    Toast t = Toast.makeText(v.getContext(), "Position " + position + " is clicked", Toast.LENGTH_SHORT);
                    t.show();

                }
            });

        }
    }

    public RecyclerAdapter(Context context,
                           View root, LinkedList<String> imagePath, LinkedList<String> title, LinkedList<String> length) {
        this.context = context;
        mInflater = LayoutInflater.from(this.context);
        this.imagePathList = imagePath;
        this.mediaTitleList = title;
        this.mediaLengthList = length;
        this.root = root;

    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.media_item, parent, false);
        return new BuildingViewHolder(mItemView, this);
    }

    //TODO: do this
    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        holder.mediaImage = holder.itemView.findViewById(R.id.imageTitle);
        holder.mediaTitle = holder.itemView.findViewById(R.id.media_title);
        holder.mediaLength = holder.itemView.findViewById(R.id.media_length);
        Uri uri = Uri.parse(imagePathList.get(position));

        holder.mediaImage.setImageURI(uri);
        holder.mediaTitle.setText(mediaTitleList.get(position));
        holder.mediaLength.setText(mediaLengthList.get(position));


    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return imagePathList.size();
    }

    public void updateResult(String roomName, int crowdness, int index){
        //this.roomNameList.set(index, roomName);
        //this.crowdnessList.set(index,crowdness);
    }

}