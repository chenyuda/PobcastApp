package de.danoeh.antennapod.fragment;

import java.util.List;

import de.danoeh.antennapod.core.gpoddernet.GpodnetService;
import de.danoeh.antennapod.core.gpoddernet.GpodnetServiceException;
import de.danoeh.antennapod.core.gpoddernet.model.GpodnetPodcast;
import de.danoeh.antennapod.fragment.gpodnet.PodcastListFragment;

/**
 * Created by cyd on 2017/3/1.
 */

public class TopFragment extends PodcastListFragment {

//    private String country="cn";
    @Override
    protected List<GpodnetPodcast> loadPodcastData(GpodnetService service,String encode) throws GpodnetServiceException {

        return service.getTopPodcastlist(encode);
    }
}
