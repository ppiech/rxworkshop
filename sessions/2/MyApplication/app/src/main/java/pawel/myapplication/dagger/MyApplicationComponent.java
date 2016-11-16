package pawel.myapplication.dagger;

import com.lookout.plugin.android.AndroidComponent;
import com.lookout.plugin.android.AndroidPluginModule;
import com.lookout.plugin.android.application.ApplicationScope;

import dagger.Component;
import pawel.myapplication.ui.UiComponent;

@ApplicationScope
@Component(modules = {
    MyApplicationModule.class,
    AndroidPluginModule.class})
public interface MyApplicationComponent extends AndroidComponent, UiComponent {

}
