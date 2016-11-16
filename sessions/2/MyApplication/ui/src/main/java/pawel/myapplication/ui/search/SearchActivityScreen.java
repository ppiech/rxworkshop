package pawel.myapplication.ui.search;

public interface SearchActivityScreen {

    interface Item {
        public void setText(String text, String imageUrl);
        public void setProgress();
    }

    void setCount(int size);

    void setProgressVisible(boolean visible);
}
