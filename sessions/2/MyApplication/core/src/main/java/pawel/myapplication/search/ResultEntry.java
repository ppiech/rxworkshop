package pawel.myapplication.search;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
abstract public class ResultEntry {

    public static ResultEntry create(String value, String imageUrl) {
        return new AutoValue_ResultEntry(value, imageUrl);
    }

    abstract public String value();
    abstract public @Nullable String imageUrl();
}
