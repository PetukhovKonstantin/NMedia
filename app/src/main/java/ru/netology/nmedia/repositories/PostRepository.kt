package ru.netology.nmedia.repositories
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun openPostById(id: Long)
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf<Post>(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Над нашими курсами работает большая команда: авторы, методисты, продюсеры, преподаватели, маркетологи, редакторы. Каждый следит за трендами на рынке, чтобы запустить качественную программу. Мы создаём продукт, которым пользуемся сами.",
            published = "18 сентября 8:36",
            likeCount = 344,
            likedByMe = false,
            shareCount = 531
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "1 июня 1:36",
            likeCount = 1,
            likedByMe = false,
            shareCount = 999,
            video = "https://www.youtube.com/watch?v=IiO3IQEUphE"
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Мы помогаем сформулировать ожидания от курсов и выбрать подходящую траекторию обучения. Студент попадает в комфортную образовательную среду: быстро вовлекается в учебный процесс, получает обратную связь от экспертов и обменивается опытом с единомышленниками.",
            published = "12 января 14:32",
            likeCount = 3,
            likedByMe = false,
            shareCount = 86
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Мы поддерживаем в течение всего обучения. Наши кураторы, эксперты и аспиранты не дают студентам сойти с дистанции. Кроме того, мы помогаем с трудоустройством: собрать портфолио, оформить резюме и пройти собеседование. Лучшие студенты стажируются у наших партнёров.",
            published = "21 августа 20:56",
            likeCount = 7,
            likedByMe = false,
            shareCount = 6
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Выпускники получают официальный документ установленного образца: удостоверение о повышении квалификации или диплом о профессиональной переподготовке. После обучения можно подать заявление на налоговый вычет и вернуть часть денег за обучение.",
            published = "21 мая 18:36",
            likeCount = 98,
            likedByMe = false,
            shareCount = 786
        ),
    )

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        posts = posts.map {post ->
            if (post.id != id) post else post.copy(
                likedByMe = !post.likedByMe,
                likeCount = if (post.likedByMe) post.likeCount - 1 else post.likeCount + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {post ->
            if (post.id != id) post else post.copy(
                shareCount = post.shareCount + 1
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Me", published = "Now")) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
    }

    override fun openPostById(id: Long) {
        posts = posts.filter { it.id == id }
        data.value = posts
    }
}