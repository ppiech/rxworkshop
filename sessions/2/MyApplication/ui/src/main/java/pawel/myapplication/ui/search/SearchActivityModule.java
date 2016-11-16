package pawel.myapplication.ui.search;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchActivityModule {

    private final SearchActivity mSearchActivity;
    public SearchActivityModule(SearchActivity mSearchActivity) {
        this.mSearchActivity = mSearchActivity;
    }

    @Provides Activity providesActivity() {
        return mSearchActivity;
    }

    @Provides SearchActivityScreen providesScreen() {
        return mSearchActivity;
    }
}
