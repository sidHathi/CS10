import java.util.ArrayList;
import java.util.List;

public class Channel {
    class Post {
        String sender;      // who posted something
        String content;     // what they posted
    }

    class Conversation {
        List<Post> posts = new ArrayList<>();    // the messages in the conversation
    }

    private List<Conversation> conversations = new ArrayList<Conversation>();

    /**
     * Starts a new conversation with some starter post. Has to be
     * synchronized so that the conversation indices are correct
     * and two conversations don't get added at once.
     * @param sender
     * @param content
     */
    synchronized public void addNewPost(String sender, String content){
        Post newPost = new Post();
        newPost.sender = sender;
        newPost.content = content;
        Conversation newConversation = new Conversation();
        newConversation.posts.add(newPost);
        conversations.add(newConversation);
    }

    /**
     * Adds a new post to an existing conversation. Has to be synchronized so that
     * the order is maintained and so that two posts don't get added at the same time.
     * @param index
     * @param sender
     * @param content
     */
    synchronized public void replyToConversation(int index, String sender, String content){
        Post reply = new Post();
        reply.sender = sender;
        reply.content = content;
        if (conversations.size() <= index){
            System.err.println("No such conversation exists");
            return;
        }

        conversations.get(index).posts.add(reply);
    }

}
