package com.b0ve.sig.adapters.extra;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.exceptions.SIGException;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.TextPost;
import com.tumblr.jumblr.types.User;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.jsoup.Jsoup;

/**
 *
 * @author borja
 */
public class AdapterTumblrWatcher extends Adapter {

    private final Thread thread;

    public AdapterTumblrWatcher(String consumerKey, String consumerSecret, String oauthToken, String oauthTokenSecret) {
        thread = new Thread() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                long lastCheck = Instant.now().getEpochSecond();

                Map<String, Integer> options = new HashMap<>();
                options.put("limit", 5);

                JumblrClient client = new JumblrClient(oauthToken, oauthTokenSecret);
                client.setToken(consumerKey, consumerSecret);
                User user = client.user();
                Blog blog = user.getBlogs().get(0);

                try {
                    while (!interrupted()) {
                        List<Post> posts = blog.posts(options);
                        for (Post post : posts) {
                            long epoch = df.parse(post.getDateGMT().substring(0, 19)).getTime() / 1000;
                            if (epoch > lastCheck) {
                                String type = post.getType().getValue();
                                switch (type) {
                                    case "text":
                                        TextPost textPost = (TextPost) post;
                                        //TODO: Este post y otros, hay que sacarlos
                                        break;
                                }
                            }
                            System.out.println(epoch);
                        }

                        lastCheck = Instant.now().getEpochSecond();
                        sleep(10000);
                    }
                } catch (InterruptedException ex) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                    handleException(new SIGException("Tumblr exception", null, ex));
                }

            }

        };
    }

    @Override
    public void iniciate() throws SIGException {
        thread.start();
    }

    @Override
    public void halt() throws SIGException {
        thread.interrupt();
    }

    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.INPUT;
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

}
