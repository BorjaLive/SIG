package com.b0ve.sig.adapters.extra;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.model.Media;
import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.request.Request;
import com.afrozaar.wordpress.wpapi.v2.request.SearchRequest;
import com.afrozaar.wordpress.wpapi.v2.response.PagedResponse;
import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;

/**
 *
 * @author borja
 */
public class AdapterWordpressWatcher extends Adapter {

    private final Thread thread;

    public AdapterWordpressWatcher(String url, String user, String pass) {
        thread = new Thread() {
            @Override
            public void run() {
                Wordpress wp = ClientFactory.fromConfig(ClientConfig.of(url, user, pass, false, false));
                String lastCheck = Instant.now().toString();
                try {
                    while (!interrupted()) {
                        PagedResponse<Post> response = wp.search(SearchRequest.Builder.aSearchRequest(Post.class)
                                .withUri(Request.POSTS)
                                .build());

                        for (Post post : response.getList()) {
                            if (post.getModified().compareTo(lastCheck) > 0) {
                                Media media = getFuckingMedia(wp, post.getFeaturedMedia());
                                sendProcess(XMLUtils.parse("<post>"
                                        + "<id>" + post.getId() + "</id>"
                                        + "<title>" + post.getTitle().getRendered() + "</title>"
                                        + "<content>" + post.getContent().getRendered() + "</content>"
                                        + "<media>" + (media == null ? "" : media.getSourceUrl()) + "</media>"
                                        + "</post>"));
                                System.out.println(post.getTitle().getRendered());
                            }
                        }

                        lastCheck = Instant.now().toString();
                        sleep(10000);
                    }
                } catch (InterruptedException ex) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                    handleException(new SIGException("Wordpress exception", wp, ex));
                }

            }

        };
    }

    private Media getFuckingMedia(Wordpress wp, Long id) {
        if (id == 0) {
            return null;
        }
        for (Iterator<Media> iterator = wp.getMedia().iterator(); iterator.hasNext();) {
            Media media = iterator.next();
            if (Objects.equals(media.getId(), id)) {
                return media;
            }
        }
        return null;
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

}
