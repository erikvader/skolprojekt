import java.util.List;
import java.util.LinkedList;

public class Feed {
    private List<Post> posts = new LinkedList<Post>();

    public void addPost(Post post) {
        posts.add(0, post);
    }

    public String renderAll(Account a) {
        return this.render(posts.size(), a);
    }

    public String renderLatest(int n, Account a) {
        return this.render(n, a);
    }

    private String render(int n, Account a) {
        String result = "";

        for (Post p : this.posts) {
            if (!a.isCurrentlyIgnoring(p.getPoster())) {
                result = result + p.render();
            }
            
            if (--n < 0) break;
        }

        return result;
    }

    public int feedSize() {
        return posts.size();
    }
}
