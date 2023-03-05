package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User curr : users){
            if(curr.getMobile().equals(mobile)){
                return curr;
            }
        }
        User newUser = new User(name, mobile);
        users.add(newUser);
        return newUser;
    }

    public Artist createArtist(String name) {
        for(Artist artist: artists){
            if(artist.getName().equals(name))
                return artist;
        }
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = createArtist(artistName);
        for(Album album : albums){
            if(album.getTitle().equals(title))
                return  album;
        }
        Album album = new Album(title);

        albums.add(album);

        List<Album> albList = new ArrayList<>();
        if(artistAlbumMap.containsKey(artist)){
            albList=artistAlbumMap.get(artist);
        }
        albList.add(album);
        artistAlbumMap.put(artist,albList);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean isAlbumPresent = false;
        Album album = new Album();
        for(Album curr : albums){
            if(curr.getTitle().equals(albumName)){
                album = curr;
                isAlbumPresent = true;
                break;
            }
        }
        if(isAlbumPresent==false){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title,length);

        songs.add(song);

        List<Song> songsList= new ArrayList<>();
        if(albumSongMap.containsKey(album)){
            songsList=albumSongMap.get(album);
        }
        songsList.add(song);
        albumSongMap.put(album,songsList);

        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(song.getLength()==length){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User currUser= new User();
        boolean flag= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag= true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(currUser);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(currUser,playlist);

        List<Playlist> userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userplaylists=userPlaylistMap.get(currUser);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(currUser,userplaylists);
        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> curr = new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle())){
                curr.add(song);
            }
        }
        playlistSongMap.put(playlist,curr);

        User currUser = new User();
        boolean flag= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                flag= true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> usersList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            usersList=playlistListenerMap.get(playlist);
        }
        usersList.add(currUser);
        playlistListenerMap.put(playlist,usersList);

        creatorPlaylistMap.put(currUser,playlist);

        List<Playlist> userPlaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userPlaylists=userPlaylistMap.get(currUser);
        }
        userPlaylists.add(playlist);
        userPlaylistMap.put(currUser,userPlaylists);

        return playlist;

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean playlistFlag =false;
        Playlist playlist = new Playlist();
        for(Playlist currPlaylist: playlists){
            if(currPlaylist.getTitle().equals(playlistTitle)){
                playlist = currPlaylist;
                playlistFlag = true;
                break;
            }
        }
        if (playlistFlag == false){
            throw new Exception("Playlist does not exist");
        }

        User currUser = new User();
        boolean userFlag = false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser = user;
                userFlag = true;
                break;
            }
        }
        if (userFlag == false){
            throw new Exception("User does not exist");
        }
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        if(!userslist.contains(currUser))
            userslist.add(currUser);
        playlistListenerMap.put(playlist,userslist);

        if(creatorPlaylistMap.get(currUser) != playlist)
            creatorPlaylistMap.put(currUser, playlist);

        List<Playlist> userPlaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            userPlaylists=userPlaylistMap.get(currUser);
        }
        if(!userPlaylists.contains(playlist)) userPlaylists.add(playlist);
        userPlaylistMap.put(currUser, userPlaylists);

        return playlist;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User currUser= new User();
        boolean userFlag= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currUser=user;
                userFlag= true;
                break;
            }
        }
        if (userFlag==false){
            throw new Exception("User does not exist");
        }

        Song song = new Song();
        boolean flag = false;
        for(Song currSong : songs){
            if(currSong.getTitle().equals(songTitle)){
                song = currSong;
                flag = true;
                break;
            }
        }
        if (flag == false){
            throw new Exception("Song does not exist");
        }

        List<User> users = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            users=songLikeMap.get(song);
        }
        if (!users.contains(currUser)){
            users.add(currUser);
            songLikeMap.put(song, users);
            song.setLikes(song.getLikes()+1);

            Album album = new Album();
            for(Album currAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(currAlbum);
                if(temp.contains(song)){
                    album = currAlbum;
                    break;
                }
            }

            Artist artist = new Artist();
            for(Artist currArtist : artistAlbumMap.keySet()){
                List<Album> temp = artistAlbumMap.get(currArtist);
                if(temp.contains(album)){
                    artist = currArtist;
                    break;
                }
            }

            artist.setLikes(artist.getLikes()+1);
        }
        return song;

    }

    public String mostPopularArtist() {
        String artName = "";
        int maxLikes = -1;
        for(Artist artist : artists){
            maxLikes = Math.max(maxLikes, artist.getLikes());
        }
        for(Artist artist : artists){
            if(maxLikes == artist.getLikes()){
                artName = artist.getName();
            }
        }
        return artName;
    }

    public String mostPopularSong() {
        String artName="";
        int maxLikes = -1;
        for(Song song : songs){
            maxLikes = Math.max(maxLikes, song.getLikes());
        }
        for(Song song : songs){
            if(maxLikes == song.getLikes())
                artName = song.getTitle();
        }
        return artName;
    }
}
