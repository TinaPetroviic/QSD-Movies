package com.example.qsdmovies.util

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState

data class PageFilter(
    val limit: Int,
    val page: Int = INITIAL_PAGE,
) {

    fun amountLoaded() = (page - 1) * limit

    fun nextPage() = copy(page = page + 1)

    fun previousPage() = copy(
        page = if (page > INITIAL_PAGE) {
            page - 1
        } else {
            INITIAL_PAGE
        }
    )

    fun isLastPageReached(currentPageSize: Int) = currentPageSize < limit

    companion object {
        const val INITIAL_PAGE = 1
    }
}

class SequencePagingSource<Value : Any>(
    private val initialKey: Int = DEFAULT_INITIAL_KEY,
    private val loadDataBlock: suspend (PageFilter) -> List<Value>
) : PagingSource<Int, Value>() {
    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        return try {
            val key = params.key ?: initialKey
            val data = loadDataBlock(PageFilter(page = key, limit = params.loadSize))
            LoadResult.Page(
                data = data,
                nextKey = if (data.size >= params.loadSize) key + 1 else null,
                prevKey = if (key <= initialKey) null else key - 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val DEFAULT_INITIAL_KEY = 1
    }
}


fun <T : Any> pager(
    config: PagingConfig,
    initialPageKey: Int = SequencePagingSource.DEFAULT_INITIAL_KEY,
    block: suspend (PageFilter) -> List<T>
) = Pager(
    config = config,
    pagingSourceFactory = {
        SequencePagingSource(initialPageKey, block)
    }
)