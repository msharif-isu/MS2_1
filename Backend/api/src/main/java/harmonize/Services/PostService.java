package harmonize.Services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import harmonize.DTOs.PostDTO;
import harmonize.Entities.Post;
import harmonize.Entities.User;
import harmonize.Entities.FeedItems.PostFeedItem;
import harmonize.Enum.FeedEnum;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.InvalidArgumentException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedException;
import harmonize.Repositories.PostRepository;
import harmonize.Repositories.UserRepository;
import jakarta.transaction.Transactional;

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
    
    public List<PostDTO> getPost(int id) {
        User user = userRepository.findReferenceById(id);

        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        List<PostDTO> posts = new ArrayList<>();

        for(Post post : user.getSentPosts())
            posts.add(new PostDTO(post));
        
        return posts;
    }

    public PostDTO sendPost(int id, String post) {
        User user = userRepository.findReferenceById(id);
        
        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

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

    @Transactional
    public PostDTO deletePost(int id, int postId) {
        User user = userRepository.findReferenceById(id);
        Post post = postRepository.findReferenceById(postId);
        
        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        if(post == null)
            throw new EntityNotFoundException("Post " + postId + " not found.");

        if(!user.getSentPosts().contains(post))
            throw new UnauthorizedException("Post " + postId + " cannot be deleted.");

        Hibernate.initialize(post.getPostFeedItems());
        user.getSentPosts().remove(post);
        userRepository.save(user);

        return new PostDTO(post);
    }
}
