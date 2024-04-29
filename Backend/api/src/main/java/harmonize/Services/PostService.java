package harmonize.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.DTOs.PostDTO;
import harmonize.Entities.Post;
import harmonize.Entities.User;
import harmonize.Entities.FeedItems.PostFeedItem;
import harmonize.Enum.FeedEnum;
import harmonize.ErrorHandling.Exceptions.InvalidArgumentException;
import harmonize.Repositories.PostRepository;
import harmonize.Repositories.UserRepository;

@Service
public class PostService {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private FeedService feedService;

    @Autowired
    PostService(UserRepository userRepository, PostRepository postRepository, FeedService feedService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.feedService = feedService;
    }

    public PostDTO sendPost(int id, String post) {
        User user = userRepository.findReferenceById(id);

        if(post.length() > Post.POST_LENGTH_MAX)
            throw new InvalidArgumentException("Post is too long");

        Post newPost = new Post(user, post);
        postRepository.save(newPost);

        for(User friend : user.getFriends()) {
            friend.getReceivedPosts().add(new PostFeedItem(FeedEnum.POST, newPost, friend));
            userRepository.save(friend);
            feedService.notifyuser(friend, newPost);
        }

        return new PostDTO(newPost);
    }
}
