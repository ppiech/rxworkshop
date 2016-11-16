package pawel.myapplication.ui;

import com.lookout.plugin.android.AndroidComponent;

import pawel.myapplication.ui.search.SearchActivityModule;
import pawel.myapplication.ui.search.SearchActivitySubcomponent;

public interface UiComponent extends AndroidComponent {
    SearchActivitySubcomponent createSearchActivitySubcomponent(SearchActivityModule module);
}
