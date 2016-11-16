package pawel.myapplication.main;

import android.app.Application;

import com.lookout.plugin.android.AndroidComponent;
import com.lookout.plugin.android.AndroidComponentProvider;
import com.lookout.plugin.android.BuildConfigModule;
import com.lookout.plugin.android.BuildConfigWrapper;
import com.lookout.plugin.android.application.ApplicationModule;

import pawel.myapplication.BuildConfig;
import pawel.myapplication.dagger.DaggerMyApplicationComponent;
import pawel.myapplication.dagger.MyApplicationComponent;

public class MyApplication extends Application implements AndroidComponentProvider {

    private MyApplicationComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerMyApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    @Override
    public AndroidComponent androidComponent() {
        return mAppComponent;
    }
}
