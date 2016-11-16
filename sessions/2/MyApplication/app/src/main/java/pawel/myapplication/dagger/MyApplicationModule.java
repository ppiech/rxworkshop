package pawel.myapplication.dagger;

import com.lookout.plugin.android.AndroidPluginModule;

import dagger.Module;
import pawel.myapplication.search.SearchModule;

@Module(includes = {AndroidPluginModule.class, SearchModule.class})
public class MyApplicationModule {


}
