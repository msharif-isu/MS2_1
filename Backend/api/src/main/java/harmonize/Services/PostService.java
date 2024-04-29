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
import harmonize.Repositories.PostRepository;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class PostService {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private RoleRepository roleRepository;
    private FeedService feedService;

    @Autowired
    PostService(UserRepository userRepository, PostRepository postRepository, RoleRepository roleRepository, FeedService feedService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.feedService = feedService;
    }

    public List<PostDTO> getPosts(int id) {
        return getPosts(id, id);
    }
    
    @Transactional
    public List<PostDTO> getPosts(int id, int posterId) {
        User user = userRepository.findReferenceById(id);
        User poster = userRepository.findReferenceById(posterId);

        if(user == null || poster == null)
            throw new EntityNotFoundException("User not found.");

        if(user.getRoles().contains(roleRepository.findByName("USER")) && user.getRoles().size() <= 1 && poster != user && !poster.getFriends().contains(user))
            throw new EntityNotFoundException("User not found.");

        List<PostDTO> posts = new ArrayList<>();

        Hibernate.initialize(poster.getSentPosts());
        for(Post post : poster.getSentPosts())
            posts.add(new PostDTO(post));
        
        return posts;
    }

    public PostDTO getPost(int id, int postId) {
        User user = userRepository.findReferenceById(id);
        Post post = postRepository.findReferenceById(postId);
        
        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        if(user.getRoles().contains(roleRepository.findByName("USER")) && user.getRoles().size() <= 1 && post.getPoster().getId() != user.getId())
            throw new EntityNotFoundException("User " + id + " not found.");

        if(post == null)
            throw new EntityNotFoundException("Post " + postId + " not found.");

        return new PostDTO(post);
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
    public PostDTO deleteSentPost(int id, int postId) {
        User user = userRepository.findReferenceById(id);
        Post post = postRepository.findReferenceById(postId);
        
        if(user == null)
            throw new EntityNotFoundException("User " + id + " not found.");

        if(post == null || !user.getSentPosts().contains(post))
            throw new EntityNotFoundException("Post " + postId + " not found.");

        Hibernate.initialize(post.getPostFeedItems());
        user.getSentPosts().remove(post);
        userRepository.save(user);

        return new PostDTO(post);
    }

    @Transactional
    public PostDTO deletePost(int postId) {
        Post post = postRepository.findReferenceById(postId);

        if(post == null)
            throw new EntityNotFoundException("Post " + postId + " not found.");

        Hibernate.initialize(post.getPoster());
        User user = post.getPoster();

        if(user == null)
            throw new EntityNotFoundException("User not found.");

        Hibernate.initialize(post.getPostFeedItems());
        user.getSentPosts().remove(post);
        userRepository.save(user);

        return new PostDTO(post);
    }
}
