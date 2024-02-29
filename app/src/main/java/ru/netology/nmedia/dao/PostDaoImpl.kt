package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getStringOrNull
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKE_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARE_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO} TEXT
        );
        """.trimIndent()
        val DDL2 = """
        CREATE TABLE ${DraftColumns.TABLE} (
            ${DraftColumns.COLUMN_ID_USER} INTEGER PRIMARY KEY,
            ${DraftColumns.COLUMN_CONTENT} TEXT NOT NULL
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKE_COUNT = "likeCount"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_SHARE_COUNT = "shareCount"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKE_COUNT,
            COLUMN_LIKED_BY_ME,
            COLUMN_SHARE_COUNT,
            COLUMN_VIDEO
        )
    }

    object DraftColumns {
        const val TABLE = "draft"
        const val COLUMN_ID_USER = "idUser"
        const val COLUMN_CONTENT = "content"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID_USER,
            COLUMN_CONTENT
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            // TODO: remove hardcoded values
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "now")
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likeCount = likeCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               shareCount = shareCount + 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun saveDraft(content: String) {
        val idUser = 0
        val values = ContentValues().apply {
            // TODO: remove hardcoded values
            put(DraftColumns.COLUMN_ID_USER, 0)
            put(DraftColumns.COLUMN_CONTENT, content)
        }
        val isNewDraft = db.query(
            DraftColumns.TABLE,
            DraftColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${DraftColumns.COLUMN_ID_USER} DESC"
        ).count > 0

        if (isNewDraft) {
            db.update(
                DraftColumns.TABLE,
                values,
                "${DraftColumns.COLUMN_ID_USER} = ?",
                arrayOf(idUser.toString()),
            )
        } else {
            db.insert(DraftColumns.TABLE, null, values)
        }
    }

    override fun getDraft(): String? {
        val idUser = 0
        val query = db.query(
            DraftColumns.TABLE,
            DraftColumns.ALL_COLUMNS,
            "${DraftColumns.COLUMN_ID_USER} = ?",
            arrayOf(idUser.toString()),
            null,
            null,
            null,
        )
        return if (query.count > 0) {
            query.use {
                it.moveToNext()
                it.getString(it.getColumnIndexOrThrow(DraftColumns.COLUMN_CONTENT))
            }
        } else {
            return null
        }
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likeCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKE_COUNT)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                shareCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARE_COUNT)),
                video = getStringOrNull(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO))
            )
        }
    }
}