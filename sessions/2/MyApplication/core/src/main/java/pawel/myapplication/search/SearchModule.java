package pawel.myapplication.search;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchModule {
//    @Provides
//    Search providesSearch(SeachStub search) {
//        return search;
//    }

    @Provides
    Search providesSearch(SeachRemote search) {
        return search;
    }

}
