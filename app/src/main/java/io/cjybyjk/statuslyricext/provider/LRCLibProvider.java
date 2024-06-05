package io.cjybyjk.statuslyricext.provider;

import android.media.MediaMetadata;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;

import io.cjybyjk.statuslyricext.provider.utils.HttpRequestUtil;

public class LRCLibProvider implements ILrcProvider {

    private static final String LRCLIB_BASE_URL = "https://lrclib.net/";
    private static final String LRCLIB_SEARCH_URL_FORMAT = LRCLIB_BASE_URL + "api/get?artist_name=%s&track_name=%s&album_name=%s&duration=%d";

    @Override
    public LyricResult getLyric(MediaMetadata data) throws IOException {
        String artist = data.getString(MediaMetadata.METADATA_KEY_ARTIST);
        String album = data.getString(MediaMetadata.METADATA_KEY_ALBUM);
        String searchUrl = String.format(
                Locale.getDefault(),
                LRCLIB_SEARCH_URL_FORMAT,
                URLEncoder.encode(artist, "UTF-8"),
                URLEncoder.encode(data.getString(MediaMetadata.METADATA_KEY_TITLE), "UTF-8"),
                URLEncoder.encode(album, "UTF-8"),
                data.getLong(MediaMetadata.METADATA_KEY_DURATION) / 1000
        );
        JSONObject searchResult;
        try {
            searchResult = HttpRequestUtil.getJsonResponse(searchUrl);
            if (searchResult != null) {
                LyricResult result = new LyricResult();
                result.mLyric = searchResult.getString("syncedLyrics");
                result.mDistance = searchResult.getInt("duration");
                return result;
            }
        } catch (JSONException e) {
            Log.e("ERROR", String.valueOf(e));
            return null;
        }
        return null;
    }
}
