package harmonize.Services;

import org.springframework.beans.factory.annotation.Autowired;
import harmonize.Repositories.PostRepository;
import harmonize.Repositories.UserRepository;

public class PostService {
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
}
