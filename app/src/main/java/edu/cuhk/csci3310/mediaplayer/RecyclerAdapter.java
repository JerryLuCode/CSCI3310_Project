package edu.cuhk.csci3310.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import edu.cuhk.csci3310.mediaplayer.ui.AudioPlayer.MusicPlayerActivity;
import edu.cuhk.csci3310.mediaplayer.ui.AudioPlayer.MyMediaPlayer;
import edu.cuhk.csci3310.mediaplayer.ui.VideoPlayerFolder.VideoPlayer;
public class RecyclerAdapter extends Adapter<RecyclerAdapter.BuildingViewHolder>  {

    private Context context;
    private LayoutInflater mInflater;
    ArrayList<MediaModel> mediaList;
    private String mediaType;
    FragmentManager fragmentManager;
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

                    //If video only send URI, if Audio send MediaModel object
                    if(mediaType == "video")
                    {
                        //Intent for starting the video player activity
                        Intent videoPlayerIntent = new Intent(context, VideoPlayer.class);
                        videoPlayerIntent.putExtra("URI", mediaList.get(position).getPath());
                        context.startActivity(videoPlayerIntent);
                    }else if(mediaType == "audio")
                    {
                        //navigate to another activity and play the song
                        MyMediaPlayer.getInstance().reset();
                        MyMediaPlayer.currentIndex = position;
                        Intent intent = new Intent(context, MusicPlayerActivity.class);
                        intent.putExtra("LIST", mediaList);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

    //Get data from host fragment/activity and update recyclerList data
    public RecyclerAdapter(Context context,
                           View root, FragmentManager fragmentManager, String mediaType, ArrayList<MediaModel> mediaList) {
        this.context = context;
        this.mediaType = mediaType;
        this.fragmentManager = fragmentManager;
        mInflater = LayoutInflater.from(this.context);
        this.root = root;
        this.mediaList = mediaList;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.media_item, parent, false);
        return new BuildingViewHolder(mItemView, this);
    }

    //Updates the Views of media_item.xml
    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        holder.mediaImage = holder.itemView.findViewById(R.id.imageTitle);
        holder.mediaTitle = holder.itemView.findViewById(R.id.media_title);
        holder.mediaLength = holder.itemView.findViewById(R.id.media_length);
        Uri uri = Uri.parse(mediaList.get(position).getImagePath());

        holder.mediaImage.setImageURI(uri);
        holder.mediaTitle.setText(mediaList.get(position).getTitle());
        holder.mediaLength.setText(mediaList.get(position).getDuration());


    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

}