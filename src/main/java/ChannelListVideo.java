import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class ChannelListVideo {
    private static final String DEVELOPER_KEY = "AIzaSyBxqO6e8l-TIk7s9Iglvf9hzcEw1jMsvws";
    private static final String APPLICATION_NAME = "API code samples";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static void listPlaylistAndVideo() throws GeneralSecurityException, IOException {
        YouTube youtube = getService();
        YouTube.Playlists.List request = youtube.playlists()
                .list(Collections.singletonList("snippet"));
        PlaylistListResponse response = request.setKey(DEVELOPER_KEY)
                .setChannelId("UCqZQJ4600a9wIfMPbYc60OQ")
                .setMaxResults(50L)
                .execute();

        List<Playlist> items = response.getItems();
        for (Playlist playlist : items) {
            String playlistId = playlist.getId();
            System.out.println("playlistId = " + playlistId);

            YouTube.PlaylistItems.List itemResponseRequest = youtube.playlistItems()
                    .list(Collections.singletonList("snippet"));
            PlaylistItemListResponse playlistResponse = itemResponseRequest.setKey(DEVELOPER_KEY)
                    .setMaxResults(50L)
                    .setPlaylistId(playlistId)
                    .execute();

            List<PlaylistItem> videos = playlistResponse.getItems();
            for (PlaylistItem video : videos) {
                PlaylistItemSnippet snippet = video.getSnippet();
                String videoId = snippet.getResourceId().getVideoId();
                String title = snippet.getTitle();
                System.out.println(videoId + " " + title);
            }
        }
    }

    public static void main(String[] args)
            throws GeneralSecurityException, IOException {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");

        listPlaylistAndVideo();

    }
}