package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            publiched = "21 мая 18:36",
            likeCount = 999,
            likedByMe = false,
            shareCount = 531_786
        )

        with(binding){
            author.text = post.author
            published.text = post.publiched
            content.text = post.content
            if (post.likedByMe) {
                like.setImageResource(R.drawable.ic_like_active)
            }
            likeCount.text = PostService.ConvertCountToShortString(post.likeCount)
            shareCount.text = PostService.ConvertCountToShortString(post.shareCount)

            like.setOnClickListener {
                if (post.likedByMe) post.likeCount-- else post.likeCount++
                likeCount.text = PostService.ConvertCountToShortString(post.likeCount)
                post.likedByMe = !post.likedByMe
                like.setImageResource(if (post.likedByMe) R.drawable.ic_like_active else R.drawable.ic_like )
            }

            share.setOnClickListener{
                post.shareCount++
                shareCount.text = PostService.ConvertCountToShortString(post.shareCount)
            }
        }
    }
}