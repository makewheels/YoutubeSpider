import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.mortbay.util.ajax.JSON;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class YoutubeUtil {
    // You need to set this value for your code to compile.
    // For example: ... DEVELOPER_KEY = "YOUR ACTUAL KEY";
    private static final String DEVELOPER_KEY = "AIzaSyBxqO6e8l-TIk7s9Iglvf9hzcEw1jMsvws";

    private static final String APPLICATION_NAME = "API code samples";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static void listPlaylistAndVideo() throws GeneralSecurityException, IOException {
        YouTube youtube = getService();
        YouTube.Playlists.List request = youtube.playlists()
                .list("snippet");
        PlaylistListResponse response = request.setKey(DEVELOPER_KEY)
                .setChannelId("UCqZQJ4600a9wIfMPbYc60OQ")
                .setMaxResults(50L)
                .setPageToken(null)
                .execute();

        List<Playlist> items = response.getItems();
        for (Playlist playlist : items) {
            String playlistId = playlist.getId();
            System.out.println("playlistId = " + playlistId);

            YouTube.PlaylistItems.List itemResponseRequest = youtube.playlistItems()
                    .list("snippet");
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

    private static void uploadVideo() throws GeneralSecurityException, IOException {
        YouTube youtubeService = getService();
        File file = new File("D:\\BaiduNetdiskDownload\\2021.12.02 曾伟婚礼\\男方家摄影师\\男方家摄影师-P2950099.MOV");
        InputStreamContent mediaContent =
                new InputStreamContent("application/octet-stream",
                        new BufferedInputStream(
                                new FileInputStream(file)));
        mediaContent.setLength(file.length());

        YouTube.Videos.Insert request = youtubeService.videos()
                .insert("snippet,status", new Video(), mediaContent);
        Video response = request.execute();
        System.out.println(response);
    }

    public static void main(String[] args)
            throws GeneralSecurityException, IOException {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");

//        listPlaylistAndVideo();
        uploadVideo();

    }
}