package pawel.myapplication.ui.search;

import com.lookout.plugin.android.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = SearchActivityModule.class)
public interface SearchActivitySubcomponent {
    void inject(SearchActivity toInject);
}
