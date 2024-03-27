package com.example.music_app1;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<PlaylistItem> playlists;

    public PlaylistAdapter(List<PlaylistItem> playlists) {
        this.playlists = playlists;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item_layout, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        PlaylistItem playlist = playlists.get(position);

        holder.imagePlaylist.setImageResource(playlist.getImageResourceId());  // Assuming "imageResourceId" is a member variable in PlaylistItem
        holder.textPlaylistName.setText(playlist.getPlaylistName());
        holder.textUserName.setText(playlist.getUserName());
        holder.textNumberOfSongs.setText(playlist.getNumberOfSongs());
        holder.textDuration.setText(playlist.getDuration());
    }

    @Override
    public int getItemCount() {
        return playlists != null ? playlists.size() : 0;
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePlaylist;
        TextView textPlaylistName;
        TextView textUserName;
        TextView textNumberOfSongs;
        TextView textDuration;

        public PlaylistViewHolder(View itemView) {
            super(itemView);

            imagePlaylist = itemView.findViewById(R.id.imagebaihat);
            textPlaylistName = itemView.findViewById(R.id.textplaylist);
            textUserName = itemView.findViewById(R.id.textuser);
            textNumberOfSongs = itemView.findViewById(R.id.textsobaihat);
            textDuration = itemView.findViewById(R.id.textsophut);
        }
    }
}
